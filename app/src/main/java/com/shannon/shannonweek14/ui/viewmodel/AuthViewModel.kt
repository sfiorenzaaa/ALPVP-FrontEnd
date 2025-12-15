package com.shannon.shannonweek14.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shannon.shannonweek14.data.dto.LoginRequest
import com.shannon.shannonweek14.data.dto.RegisterRequest
import com.shannon.shannonweek14.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val loginResponse = MutableLiveData<String>()      // token
    val registerSuccess = MutableLiveData<Boolean>()

    fun login(email: String, password: String, function: () -> Unit) {
        loading.value = true

        viewModelScope.launch {
            try {
                val result = repo.login(LoginRequest(email, password))
                loginResponse.value = result.data.token
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
