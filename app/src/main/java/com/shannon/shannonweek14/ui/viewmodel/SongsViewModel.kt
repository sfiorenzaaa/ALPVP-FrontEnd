package com.shannon.shannonweek14.ui.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shannon.shannonweek14.ui.model.Songs
import com.shannon.shannonweek14.data.repository.SongsQuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SongsViewModel(private val token: String) : ViewModel() {

    private val repo = SongsQuizRepository(token)

    private val _songsList = MutableStateFlow<List<Songs>>(emptyList())
    val songsList: StateFlow<List<Songs>> get() = _songsList

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun loadSongs() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repo.getSongsQuiz() // Response<SongsResponse>
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _songsList.value = body.data // body.data: List<Songs>
                    } else {
                        _error.value = "Response body is null"
                    }
                } else {
                    _error.value = "Error ${response.code()}: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }


}
