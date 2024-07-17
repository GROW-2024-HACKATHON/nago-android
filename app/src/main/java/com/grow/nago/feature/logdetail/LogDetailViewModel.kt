package com.grow.nago.feature.logdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grow.nago.feature.log.LogSideEffect
import com.grow.nago.feature.log.LogUiState
import com.grow.nago.local.sharedpreferences.NagoSharedPreferences
import com.grow.nago.remote.RetrofitBuilder
import com.grow.nago.remote.request.ReportGetRequest
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LogDetailViewModel: ViewModel() {

    private val _state = MutableStateFlow(LogDetailUiState())
    val state = _state.asStateFlow()

    fun load(id: Int) = viewModelScope.launch {
        kotlin.runCatching {
            RetrofitBuilder.reportService.getReport(
                id = id
            )
        }.onSuccess { response ->
            _state.update {
                it.copy(
                    reportData = response.data
                )
            }
//            _sideEffect.send(LogSideEffect.SuccessLoad)
        }.onFailure {
            it.printStackTrace()
        }
    }

}