package com.shannon.shannonweek14.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shannon.shannonweek14.dto.CreateEventRequest
import com.shannon.shannonweek14.ui.model.Event
import com.shannon.shannonweek14.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel(private val token: String) : ViewModel() {

    private val repo = EventRepository(token)

    private val _publicEvents = MutableStateFlow<List<Event>>(emptyList())
    val publicEvents: StateFlow<List<Event>> = _publicEvents

    private val _myEvents = MutableStateFlow<List<Event>>(emptyList())
    val myEvents: StateFlow<List<Event>> = _myEvents

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _pendingEvents = MutableStateFlow<List<Event>>(emptyList())
    val pendingEvents: StateFlow<List<Event>> = _pendingEvents

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin


    fun loadPublicEvents() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val events = repo.getPublicEvents()
                _publicEvents.value = events
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadMyEvents() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val events = repo.getMyEvents()
                _myEvents.value = events
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun createEvent(title: String, description: String, date: String, onSuccess: () -> Unit) {
        _loading.value = true
        viewModelScope.launch {
            try {
                repo.createEvent(
                    CreateEventRequest(
                        title = title,
                        description = description,
                        date = date
                    )
                )
                _error.value = null
                loadMyEvents()
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun approveEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                repo.updateEventStatus(eventId, "APPROVE")
                loadPendingEvents()
                loadPublicEvents()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun rejectEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                repo.updateEventStatus(eventId, "REJECT")
                loadPendingEvents()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun checkIsAdmin() {
        viewModelScope.launch {
            try {
                val userProfile = repo.getMyEvents()
                _isAdmin.value = true
            } catch (e: Exception) {
                _isAdmin.value = false
            }
        }
    }

    fun loadPendingEvents() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val result = repo.getPendingEvents()

                _pendingEvents.value = result
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error loading pending events: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
}