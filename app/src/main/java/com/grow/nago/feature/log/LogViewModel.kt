package com.grow.nago.feature.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grow.nago.local.sharedpreferences.NagoSharedPreferences
import com.grow.nago.remote.RetrofitBuilder
import com.grow.nago.remote.request.ReportGetRequest
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LogViewModel: ViewModel() {

    private val _sideEffect = Channel<LogSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val _state = MutableStateFlow(LogUiState())
    val state = _state.asStateFlow()

    fun load() = viewModelScope.launch {
        val sharedPreferences = NagoSharedPreferences.getNagoSharedPreferences()
        kotlin.runCatching {
            RetrofitBuilder.reportService.getAllReport(
                name = sharedPreferences.myName,
                phone = sharedPreferences.myTel,
                email = sharedPreferences.myEmail
            )
        }.onSuccess { response ->
            _state.update {
                it.copy(
                    reportData = response.data
                )
            }
            _sideEffect.send(LogSideEffect.SuccessLoad)
        }.onFailure {
            it.printStackTrace()
        }
    }

    fun removeReport(id: Int) = viewModelScope.launch {
        kotlin.runCatching {
            RetrofitBuilder.reportService.deleteReport(id)
        }.onSuccess {
            _state.update {
                it.copy(
                    reportData = it.reportData.toMutableList().apply {
                        removeIf {
                            it.id == id
                        }
                    }
                )
            }
            _sideEffect.send(LogSideEffect.SuccessDelete)
        }.onFailure {
            it.printStackTrace()
        }
    }

}