package com.grow.nago.feature.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import android.location.Geocoder
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.grow.nago.R
import com.grow.nago.root.NavGroup
import com.grow.nago.ui.animation.bounceClick
import com.grow.nago.ui.theme.Black
import com.grow.nago.ui.theme.Orange300
import com.grow.nago.ui.theme.White
import com.grow.nago.ui.theme.subtitle1
import com.grow.nago.ui.theme.subtitle2
import com.grow.nago.ui.theme.subtitle3
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Locale

const val TAG = "TAG"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    navVisibleChange: (Boolean) -> Unit
) {
    // 카메라
    val context = LocalContext.current
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    var camImage: Bitmap? by remember { mutableStateOf(null) }
    var camSecondImage: Bitmap? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    var nowPage by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = true) {
        navVisibleChange(false)
    }

    LifecycleStartEffect(key1 = true) {
        navVisibleChange(false)
        onStopOrDispose {
            navVisibleChange(true)
        }
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val permissionGranted = permissions.values.reduce { aac, isPermissionGranted ->
            Log.d(TAG, "CamScreen: $aac $isPermissionGranted")
            aac == isPermissionGranted
        }

//        Log.d("TAG", "Signup: $it")
//        if (!it) { return@rememberLauncherForActivityResult }
        Log.d(TAG, "CamScreen: 여기 권환 미션받음")
        if (permissionGranted) {
            Log.d(TAG, "CamScreen: 여기 권환 미션받음2")
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@rememberLauncherForActivityResult
            }
        } else {
            return@rememberLauncherForActivityResult
        }
    }

    AnimatedVisibility(
        visible = permissionState.status.isGranted.not() && nowPage == 0,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            Log.d("TAG", "Cam: $it")
            if (it.not())
                Toast.makeText(context, "카메라 권환에 동의하지 않으면 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
        LaunchedEffect(true) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }
    AnimatedVisibility(
        visible = permissionState.status.isGranted && nowPage == 0 && camImage == null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setBackgroundColor(Color.BLACK)
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                }
            )
            CameraButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
                onClick = {
                    coroutineScope.launch {
                        try {
                            Log.d(
                                TAG,
                                "CameraScreen: ${cameraController.isImageCaptureEnabled}"
                            )
                            if (cameraController.isImageCaptureEnabled) {
                                Log.d(TAG, "CamScreen: qwewqeqwewqe 호출됨")
                                val mainExecutor = ContextCompat.getMainExecutor(context)
                                cameraController.takePicture(
                                    mainExecutor,
                                    @ExperimentalGetImage object :
                                        ImageCapture.OnImageCapturedCallback() {
                                        override fun onCaptureSuccess(image: ImageProxy) {
                                            Log.d(
                                                "LOG",
                                                "onCaptureSuccess: ${image.height} ${image.width}"
                                            )
                                            super.onCaptureSuccess(image)
                                            Log.d(
                                                TAG,
                                                "onCaptureSuccess: ${image.imageInfo.rotationDegrees}"
                                            )
                                            val bitmap = imageProxyToBitmap(image)!!
                                            val rotateMatrix = Matrix()
                                            rotateMatrix.postRotate(image.imageInfo.rotationDegrees.toFloat())
                                            nowPage = 1
                                            camImage = Bitmap.createBitmap(
                                                bitmap,
                                                0,
                                                0,
                                                bitmap.width,
                                                bitmap.height,
                                                rotateMatrix,
                                                false
                                            )
                                            Log.d(TAG, "onCaptureSuccess: $camImage")
                                            //                                camViewModel.postImage(camImage)
                                            //                                camViewModel.nextPage()
//                                                camViewModel.nextNowPage()
//                                                bottomNavVisible(false)
//                                                Log.d(TAG, "onCaptureSuccess: ${camState.textPage}")
//                                                Log.d(TAG, "onCaptureSuccess: $textPage")
                                        }

                                        override fun onError(exception: ImageCaptureException) {
                                            super.onError(exception)
                                            exception.printStackTrace()
                                        }
                                    })
                            }
                        } catch (e: java.lang.Exception) {
                            Log.d(TAG, "CamScreen: 알파노")
                        }
                    }
                }
            )
        }
    }

    AnimatedVisibility(
        visible = camImage != null && nowPage == 1,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .height(44.dp)
                    .width(52.dp)
                    .bounceClick(
                        onClick = {
                            coroutineScope.launch {
                                Log.d(TAG, "CameraScreen: log $nowPage")
                                nowPage = 2
                            }
                        }
                    )
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "확인",
                    color = Black,
                    style = subtitle3,
                    textAlign = TextAlign.Center
                )
            }
        }
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = BitmapPainter(
                image = camImage?.asImageBitmap() ?: context.getDrawable(R.drawable.test)!!
                    .toBitmap().asImageBitmap()
            ),
            contentDescription = "촬영된 사진"
        )
    }

    AnimatedVisibility(
        visible = nowPage == 2,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LaunchedEffect(key1 = true) {
            delay(4000)
            nowPage = 3
        }
        LoadingScreen()
    }

    AnimatedVisibility(
        visible = nowPage == 3,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        CountScreen(
            onCountExit = {
                nowPage = 4
            }
        )
    }

    AnimatedVisibility(
        visible = nowPage == 4 && camSecondImage == null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setBackgroundColor(Color.BLACK)
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                }
            )
            CameraButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
                onClick = {
                    coroutineScope.launch {
                        try {
                            Log.d(
                                TAG,
                                "CameraScreen: ${cameraController.isImageCaptureEnabled}"
                            )
                            if (cameraController.isImageCaptureEnabled) {
                                Log.d(TAG, "CamScreen: qwewqeqwewqe 호출됨")
                                val mainExecutor = ContextCompat.getMainExecutor(context)
                                cameraController.takePicture(
                                    mainExecutor,
                                    @ExperimentalGetImage object :
                                        ImageCapture.OnImageCapturedCallback() {
                                        override fun onCaptureSuccess(image: ImageProxy) {
                                            Log.d(
                                                "LOG",
                                                "onCaptureSuccess: ${image.height} ${image.width}"
                                            )
                                            super.onCaptureSuccess(image)
                                            Log.d(
                                                TAG,
                                                "onCaptureSuccess: ${image.imageInfo.rotationDegrees}"
                                            )
                                            val bitmap = imageProxyToBitmap(image)!!
                                            val rotateMatrix = Matrix()
                                            rotateMatrix.postRotate(image.imageInfo.rotationDegrees.toFloat())
                                            nowPage = 5
                                            camSecondImage = Bitmap.createBitmap(
                                                bitmap,
                                                0,
                                                0,
                                                bitmap.width,
                                                bitmap.height,
                                                rotateMatrix,
                                                false
                                            )
                                            Log.d(TAG, "onCaptureSuccess: $camSecondImage")
                                            //                                camViewModel.postImage(camImage)
                                            //                                camViewModel.nextPage()
//                                                camViewModel.nextNowPage()
//                                                bottomNavVisible(false)
//                                                Log.d(TAG, "onCaptureSuccess: ${camState.textPage}")
//                                                Log.d(TAG, "onCaptureSuccess: $textPage")
                                        }

                                        override fun onError(exception: ImageCaptureException) {
                                            super.onError(exception)
                                            exception.printStackTrace()
                                        }
                                    })
                            }
                        } catch (e: java.lang.Exception) {
                            Log.d(TAG, "CamScreen: 알파노")
                        }
                    }
                }
            )
        }
    }

    AnimatedVisibility(
        visible = camSecondImage != null && nowPage == 5,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "불법 주정차",
                color = Black,
                style = subtitle1,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .height(44.dp)
                    .width(52.dp)
                    .bounceClick(
                        onClick = {
                            coroutineScope.launch {
                                Log.d(TAG, "CameraScreen: log $nowPage")
                                nowPage = 6
                            }
                        }
                    )
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "확인",
                    color = Black,
                    style = subtitle3,
                    textAlign = TextAlign.Center
                )
            }
        }
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = BitmapPainter(
                image = camSecondImage?.asImageBitmap() ?: context.getDrawable(R.drawable.test)!!
                    .toBitmap().asImageBitmap()
            ),
            contentDescription = "촬영된 사진"
        )
    }




}

@Composable
fun CountScreen(
    onCountExit: () -> Unit
) {
    var time by remember { mutableStateOf(10) }
    LaunchedEffect(key1 = true) {
        while (true) {
            delay(1000)
            time -= 1
            if (time <= 0) {
                onCountExit()
                break
            }
        }
    }

    Scaffold(
        topBar = {

        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color(0xFF2B2B2B))
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "${time}초\n뒤에 다시 촬영해주세요",
                color = White,
                style = subtitle1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LoadingScreen(

) {
    var textPage by remember { mutableStateOf(0) }
    LaunchedEffect(key1 = true) {
        (1..2).forEach { _ ->
            delay(2000)
            textPage += 1
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = textPage == 0,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Text(
                            text = "사진을 ",
                            color = Black,
                            style = subtitle2,
                        )
                        Text(
                            text = "업로드",
                            color = Orange300,
                            style = subtitle2,
                        )
                    }
                    Text(
                        text = "하고 있어요",
                        color = Black,
                        style = subtitle2,
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = textPage == 1,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "사진의 상항을",
                        color = Black,
                        style = subtitle2,
                    )
                    Row {

                        Text(
                            text = "분석",
                            color = Orange300,
                            style = subtitle2,
                        )
                        Text(
                            text = "하고 있어요",
                            color = Black,
                            style = subtitle2,
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = textPage == 2,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "상황에 맞는 제목과 내용을",
                        color = Black,
                        style = subtitle2,
                    )
                    Row {
                        Text(
                            text = "생성",
                            color = Orange300,
                            style = subtitle2,
                        )
                        Text(
                            text = "하고 있어요",
                            color = Black,
                            style = subtitle2,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier.bounceClick(onClick)) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = White,
                    shape = CircleShape
                )
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_normal_camera),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Black)
            )
        }
    }
}

fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
    //https://developer.android.com/reference/android/media/Image.html#getFormat()
    //https://developer.android.com/reference/android/graphics/ImageFormat#JPEG
    //https://developer.android.com/reference/android/graphics/ImageFormat#YUV_420_888
    if (imageProxy.format == ImageFormat.JPEG) {
        val buffer = imageProxy.planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        return bitmap
    }
    else if (imageProxy.format == ImageFormat.YUV_420_888) {
        val yBuffer = imageProxy.planes[0].buffer // Y
        val uBuffer = imageProxy.planes[1].buffer // U
        val vBuffer = imageProxy.planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, imageProxy.width, imageProxy.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
        val imageBytes = out.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return bitmap
    }
    return null
}