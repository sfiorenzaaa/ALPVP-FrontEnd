package com.shannon.shannonweek14.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            // Simulasi login, ganti dengan repo kalau ada
            val success = email == "test@test.com" && password == "123456"
            onResult(success)
        }
    }
}
