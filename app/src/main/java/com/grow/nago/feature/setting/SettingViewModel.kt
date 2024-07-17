package com.grow.nago.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grow.nago.local.sharedpreferences.NagoSharedPreferences
import kotlinx.coroutines.launch

class SettingViewModel: ViewModel() {

    fun removeData() = viewModelScope.launch {
        NagoSharedPreferences.getNagoSharedPreferences().apply {
            myName = ""
            myTel = ""
            myEmail = ""
        }
    }
}