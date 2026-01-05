package com.shannon.shannonweek14.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shannon.shannonweek14.dto.LoginRequest
import com.shannon.shannonweek14.service.ApiConfig
import com.shannon.shannonweek14.dto.LoginResponse
import com.shannon.shannonweek14.dto.RegisterRequest
import com.shannon.shannonweek14.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse
    val registerSuccess = MutableLiveData<Boolean>()

    fun login(email: String, password: String) {
        loading.value = true

        viewModelScope.launch {
            try {

                val apiService = ApiConfig.auth()
                val response = apiService.login(LoginRequest(email, password))
                if (response != null) {
                    _loginResponse.value = response.data
                } else {
                    error.value = "Login Gagal"
                }
            } catch (e: Exception) {
                error.value = e.message
            }
            loading.value = false
        }
    }

    fun register(name: String, email: String, password: String) {
        loading.value = true

        viewModelScope.launch {
            try {
                // Panggil repository dengan RegisterRequest
                repo.register(RegisterRequest(name, email, password))
                registerSuccess.value = true
            } catch (e: Exception) {
                error.value = e.message
            }
            loading.value = false
        }
    }

}
