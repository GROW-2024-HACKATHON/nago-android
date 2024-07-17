package com.grow.nago.feature.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.grow.nago.R
import com.grow.nago.root.NavGroup
import com.grow.nago.ui.animation.bounceClick
import com.grow.nago.ui.component.NagoButton
import com.grow.nago.ui.component.NagoButtonSelectMenu
import com.grow.nago.ui.component.NagoTextField
import com.grow.nago.ui.theme.Black
import com.grow.nago.ui.theme.Gray100
import com.grow.nago.ui.theme.Orange300
import com.grow.nago.ui.theme.Red300
import com.grow.nago.ui.theme.White
import com.grow.nago.ui.theme.subtitle1
import com.grow.nago.ui.theme.subtitle2
import com.grow.nago.ui.theme.subtitle3
import com.grow.nago.ui.utiles.CollectAsSideEffect
import com.grow.nago.ui.utiles.drive
import com.grow.nago.ui.utiles.life
import com.grow.nago.ui.utiles.parking
import com.grow.nago.ui.utiles.safety
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Locale

const val TAG = "TAG"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    viewModel: CameraViewModel = viewModel(),
    navController: NavController,
    navVisibleChange: (Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // 카메라
    val context = LocalContext.current
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    var camImage: Bitmap? by remember { mutableStateOf(null) }
    var camSecondImage: Bitmap? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }
    var location by remember { mutableStateOf("") }

    var nowPage by remember { mutableStateOf(0) }

    viewModel.sideEffect.CollectAsSideEffect {
        when(it) {
            is CameraSideEffect.SuccessParking -> {
                coroutineScope.launch {
                    nowPage = 3
                }
            }
            is CameraSideEffect.SuccessUpload -> {
                nowPage = 6
            }
            is CameraSideEffect.Error -> {
                coroutineScope.launch {
                    Toast.makeText(context, it.throwable.message, Toast.LENGTH_SHORT).show()
                }
            }
            is CameraSideEffect.FinishReport -> {
                navController.popBackStack()
            }
        }
    }

    LaunchedEffect(key1 = true) {
        navVisibleChange(false)
    }

    LifecycleStartEffect(key1 = true) {
        navVisibleChange(false)
        onStopOrDispose {
            navVisibleChange(true)
        }
    }

    // 위치
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
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
            fusedLocationClient.lastLocation.addOnSuccessListener {
                Log.d(TAG, "CamScreen: 요청 성공")
                try {
                    val geoCoder = Geocoder(context, Locale.getDefault())
                    latitude = it.latitude
                    longitude = it.longitude
                    val address = geoCoder.getFromLocation(it.latitude, it.longitude, 3)
                    if (address != null) {
//                        Log.d(TAG, "CamScreen: ${address[0].getAddressLine(0)}")
                        location = address[0].getAddressLine(0)
                    }
                    Log.d(TAG, "CamScreen: $address")
                } catch (e: Exception) {
//                    context.shortToast("위치를 불러오는 것을 실패하였습니다.")
                }
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
                        setBackgroundColor(android.graphics.Color.BLACK)
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
                                viewModel.uploadImage(camImage!!)
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
                        setBackgroundColor(android.graphics.Color.BLACK)
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
                                viewModel.uploadSecondImage(camSecondImage!!)
                                nowPage = 2
//                                nowPage = 6
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

    AnimatedVisibility(
        visible = nowPage == 6,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LaunchedEffect(key1 = true) {
            launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
        DeclarationScreen(
            classification = state.reportResponse.large,
            category = state.reportResponse.small,
            title = state.reportResponse.title,
            content = state.reportResponse.content,
            firstImage = camImage?: context.getDrawable(R.drawable.test)!!.toBitmap(),
            secondImage = camSecondImage,
            onClickUpload = { classification, category, title, content, firstImage, secondImage ->
                viewModel.finishReport(
                    lat = latitude.toString(),
                    lng = longitude.toString(),
                    address = location,
                    title = title,
                    content = content,
                    large = classification,
                    small = category
                )
            }
        )
    }



}
@Composable
fun DeclarationScreen(
    classification: String,
    category: String,
    title: String,
    content: String,
    firstImage: Bitmap,
    secondImage: Bitmap?,
    onClickUpload: (
        classification: String,
        category: String,
        title: String,
        content: String,
        firstImage: Bitmap,
        secondImage: Bitmap?,
    ) -> Unit
) {
    var isShowDialog by remember { mutableStateOf(false) }
    var categoryText by remember { mutableStateOf(category) }
    var classificationText by remember { mutableStateOf(classification) }
    var titleText by remember { mutableStateOf(title) }
    var contentText by remember { mutableStateOf(content) }

    LaunchedEffect(key1 = classificationText) {
        val isSuccess = when {
            classificationText == "안전" && safety.contains(categoryText) -> true
            classificationText == "생활불편" && life.contains(categoryText)  -> true
            classificationText == "자동차 교통위반" && drive.contains(categoryText) -> true
            classificationText == "불법주정차" && parking.contains(categoryText) -> true
            else -> false
        }
        if (isSuccess.not()) {
            categoryText = ""
        }
    }

    if (isShowDialog) {
        Dialog(
            onDismissRequest = { isShowDialog = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .background(
                        color = White,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Spacer(modifier = Modifier.height(36.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "정말로 신고하시겠어요?",
                    style = subtitle1,
                    color = Black
                )
                Spacer(modifier = Modifier.height(22.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    CameraDialogButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp),
                        text = "취소하기",
                        onClick = {
                            isShowDialog = false
                        },
                        textColor = Black,
                        backgroundColor = Gray100
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    CameraDialogButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp),
                        text = "신고하기",
                        onClick = {
                            onClickUpload(classification, category, title, content, firstImage, secondImage)
                        },
                        textColor = White,
                        backgroundColor = Red300
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))


            }
        }
    }

    Scaffold(
        bottomBar = {
            Column {
                NagoButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 20.dp),
                    text = "신고하기",
                    onClick = {
                        isShowDialog = true
                    },
                    enabled = classificationText.isNotEmpty() &&
                            categoryText.isNotEmpty() &&
                            titleText.isNotEmpty() &&
                            contentText.isNotEmpty()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            DeclarationCard(
                modifier = Modifier.fillMaxWidth(),
                title = "분류",
                content = {
                    NagoButtonSelectMenu(
                        modifier = Modifier.height(48.dp),
                        text = classificationText,
                        itemList = listOf("안전", "생활불편", "자동차 교통위반", "불법주정차"),
                        hint = classificationText,
                        onSelectItemListener = {
                            classificationText = it
                        }
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            DeclarationCard(
                modifier = Modifier.fillMaxWidth(),
                title = "카테고리",
                content = {
                    NagoButtonSelectMenu(
                        modifier = Modifier.height(48.dp),
                        text = categoryText,
                        itemList = when (classificationText) {
                            "안전" -> safety
                            "생활불편" -> life
                            "자동차 교통위반" -> drive
                            "불법주정차" -> parking
                            else -> listOf()
                        },
                        hint = categoryText,
                        onSelectItemListener = {
                            categoryText = it
                        }
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            DeclarationCard(
                modifier = Modifier.fillMaxWidth(),
                title = "제목",
                content = {
                    NagoTextField(
                        value = titleText,
                        hint = "신고할 제목을 입력하세요",
                        onValueChange = {
                            titleText = it
                        },
                        textStyle = subtitle3
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            DeclarationCard(
                modifier = Modifier.fillMaxWidth(),
                title = "내용",
                content = {
                    NagoTextField(
                        modifier = Modifier.heightIn(min = 108.dp),
                        value = contentText,
                        hint = "신고할 내용을 입력하세요",
                        onValueChange = {
                            contentText = it
                        },
                        singleLine = false,
                        textStyle = subtitle3
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            DeclarationCard(
                modifier = Modifier.fillMaxWidth(),
                title = if (secondImage == null) "사진" else "불법 주정차 사진 1분 전",
                content = {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        painter = BitmapPainter(
                            image = firstImage.asImageBitmap()
                        ),
                        contentDescription = "촬영된 사진"
                    )
                }
            )
            if (secondImage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                DeclarationCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "불법 주정차 사진 1분 후",
                    content = {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            painter = BitmapPainter(
                                image = secondImage.asImageBitmap()
                            ),
                            contentDescription = "촬영된 사진"
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun DeclarationCard(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = title,
            color = Black,
            style = subtitle3
        )
        Spacer(modifier = Modifier.height(4.dp))
        content()
    }
}

@Composable
fun CountScreen(
    onCountExit: () -> Unit
) {
    var time by remember { mutableStateOf(1) }
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
                .background(Color(0xFF2B2B2B))
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
            delay(3000)
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
                        text = "사진의 상황을",
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

@Composable
private fun CameraDialogButton(
    modifier: Modifier,
    text: String,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .bounceClick(
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = text,
                color = textColor,
                style = subtitle3.copy(
                    fontWeight = FontWeight.Bold
                )
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