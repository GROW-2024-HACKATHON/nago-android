package com.grow.nago.feature.logdetail

import com.grow.nago.remote.response.ReportResponse

data class LogDetailUiState(
    val reportData: ReportResponse = ReportResponse(0, "", null, "", "", "", "", "", "", "", "", "", "", listOf(0, 0,0,0,0,0,0,0,0), "")
)
