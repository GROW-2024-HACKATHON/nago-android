package com.grow.nago.feature.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grow.nago.local.sharedpreferences.NagoSharedPreferences
import com.grow.nago.remote.RetrofitBuilder
import com.grow.nago.remote.request.ReportGetRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LogViewModel: ViewModel() {

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
        }.onFailure {
            it.printStackTrace()
        }
    }

}