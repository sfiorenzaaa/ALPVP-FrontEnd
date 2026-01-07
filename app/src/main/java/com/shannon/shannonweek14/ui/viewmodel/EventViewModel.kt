package com.shannon.shannonweek14.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shannon.shannonweek14.dto.CreateEventRequest
import com.shannon.shannonweek14.repository.EventRepository
import com.shannon.shannonweek14.ui.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventViewModel(private val token: String) : ViewModel() {

    private val repository = EventRepository(token)

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()


    private val _publicEvents = MutableStateFlow<List<Event>>(emptyList())
    val publicEvents: StateFlow<List<Event>> = _publicEvents.asStateFlow()

    private val _myEvents = MutableStateFlow<List<Event>>(emptyList())
    val myEvents: StateFlow<List<Event>> = _myEvents.asStateFlow()

    private val _pendingEvents = MutableStateFlow<List<Event>>(emptyList())
    val pendingEvents: StateFlow<List<Event>> = _pendingEvents.asStateFlow()


    private val _joinMessage = MutableStateFlow<String?>(null)
    val joinMessage: StateFlow<String?> = _joinMessage.asStateFlow()


    fun loadPublicEvents() {
        _loading.value = true
        viewModelScope.launch {
            try {
                _publicEvents.value = repository.getPublicEvents()
            try {   
                val events = repo.getPublicEvents()
                _publicEvents.value = events
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load public: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadMyEvents() {
        viewModelScope.launch {
            try {
                _myEvents.value = repository.getMyEvents()
            } catch (e: Exception) {
                _error.value = "Failed to load my events: ${e.message}"
            }
        }
    }

    fun loadPendingEvents() {
        viewModelScope.launch {
            try {
                _pendingEvents.value = repository.getPendingEvents()
            } catch (e: Exception) {
                _error.value = "Failed to load pending: ${e.message}"
            }
        }
    }

    fun createEvent(title: String, desc: String, date: String, onSuccess: () -> Unit) {
        _loading.value = true
        viewModelScope.launch {
            try {
                repository.createEvent(CreateEventRequest(title, desc, date))
                loadPublicEvents()
                loadMyEvents()
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Failed to create: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }


    fun joinEvent(eventId: Int) {
        viewModelScope.launch {
            // _loading.value = true // Opsional: kalau mau loading indicator muncul
            try {
                // 1. Panggil API Join
                repository.joinEvent(token, eventId)

                _joinMessage.value = "Berhasil Join Event!"

                _publicEvents.value = _publicEvents.value.map { event ->
                    if (event.id == eventId) {
                        event.copy(isJoined = true)
                    } else {
                        event
                    }
                }

                loadPublicEvents()


            } catch (e: Exception) {
                if (e.message?. contains("409")==true || e.message?.contains("400") == true) {
                    _error.value = "Kamu sudah join event ini"

                    _publicEvents.value = _publicEvents.value.map { event ->
                        if (event.id == eventId) {
                            event.copy(isJoined = true)
                        } else {
                            event
                        }
                    }
                } else {
                    _error.value = "Gagal join: ${e.message}"
                }
            } finally {
                _loading.value = false
            }
        }
    }

    fun approveEvent(eventId: Int) {
        updateStatus(eventId, "APPROVE")
    }

    fun rejectEvent(eventId: Int) {
        updateStatus(eventId, "REJECT")
    }

    private fun updateStatus(eventId: Int, status: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                repository.updateEventStatus(eventId, status)
                loadPendingEvents()
                loadPublicEvents()
            } catch (e: Exception) {
                _error.value = "Failed to update: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}