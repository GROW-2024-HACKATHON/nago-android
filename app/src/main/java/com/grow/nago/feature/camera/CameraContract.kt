package com.grow.nago.feature.camera

import com.grow.nago.remote.response.ReportResponse

data class CameraUiState(
    val reportResponse: ReportResponse = ReportResponse(0, "", null, "", "", "", "", "", "", "", "", "", "", listOf())
)

sealed interface CameraSideEffect {
    data object SuccessParking: CameraSideEffect
    data object SuccessUpload: CameraSideEffect
}