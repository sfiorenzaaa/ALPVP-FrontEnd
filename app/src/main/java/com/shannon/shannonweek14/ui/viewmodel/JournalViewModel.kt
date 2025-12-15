package com.shannon.shannonweek14.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shannon.shannonweek14.ui.model.Journal
import com.shannon.shannonweek14.repository.JournalRepository
import com.shannon.shannonweek14.service.CreateJournalRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JournalViewModel(private val token: String) : ViewModel() {

    private val repo = JournalRepository(token)

    private val _journals = MutableStateFlow<List<Journal>>(emptyList())
    val journals: StateFlow<List<Journal>> = _journals

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadJournals() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repo.getJournals()
                _journals.value = response
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun createJournal(content: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                repo.createJournal(CreateJournalRequest(content))
                loadJournals() // refresh list
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun deleteJournal(id: Int) {
        _loading.value = true
        viewModelScope.launch {
            try {
                repo.deleteJournal(id)
                loadJournals()
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }
}
