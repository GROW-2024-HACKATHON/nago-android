package com.grow.nago.feature.log

import com.grow.nago.remote.response.ReportResponse

data class LogUiState(
    val reportData: List<ReportResponse> = emptyList()
)

sealed interface LogSideEffect {
    data object SuccessLoad: LogSideEffect
}