package com.grow.nago.feature.camera

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grow.nago.remote.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink

class CameraViewModel: ViewModel() {

    private val _sideEffect = Channel<CameraSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val _state = MutableStateFlow(CameraUiState())
    val state = _state.asStateFlow()


    fun uploadImage(image: Bitmap) = viewModelScope.launch(Dispatchers.IO) {
        val bitmapRequestBody = BitmapRequestBody(image)
        //multipart/form-data
        val bitmapMultipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("image", "CamView.jpeg", bitmapRequestBody)
        kotlin.runCatching {
            RetrofitBuilder.reportService.reportUpload(bitmapMultipartBody)
        }.onSuccess { response ->
            _state.update {
                it.copy(
                    reportResponse = response.data
                )
            }
            _sideEffect.send( if(response.data.large == "불법주정차") CameraSideEffect.SuccessParking else CameraSideEffect.SuccessUpload)
        }.onFailure {
            it.printStackTrace()
        }
    }
}


class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
    override fun contentType(): MediaType = "image/jpeg".toMediaType()

    override fun writeTo(sink: BufferedSink) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, sink.outputStream())
    }
}