package com.grow.nago.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grow.nago.local.sharedpreferences.NagoSharedPreferences
import kotlinx.coroutines.launch

class EmailViewModel: ViewModel() {

    fun saveData(phone: String, email: String, name: String) = viewModelScope.launch {
        with(NagoSharedPreferences.getNagoSharedPreferences()) {
            myName = name
            myEmail = email
            myTel = phone
        }
    }
}