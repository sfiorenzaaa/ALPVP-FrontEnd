package com.shannon.shannonweek14.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
import com.shannon.shannonweek14.data.dto.CreateEventRequest
import com.shannon.shannonweek14.data.model.Event
=======
import com.shannon.shannonweek14.dto.CreateEventRequest
import com.shannon.shannonweek14.ui.model.Event
>>>>>>> Stashed changes
=======
import com.shannon.shannonweek14.dto.CreateEventRequest
import com.shannon.shannonweek14.ui.model.Event
>>>>>>> Stashed changes
=======
import com.shannon.shannonweek14.dto.CreateEventRequest
import com.shannon.shannonweek14.ui.model.Event
>>>>>>> Stashed changes
=======
import com.shannon.shannonweek14.dto.CreateEventRequest
import com.shannon.shannonweek14.ui.model.Event
>>>>>>> Stashed changes
=======
import com.shannon.shannonweek14.dto.CreateEventRequest
import com.shannon.shannonweek14.ui.model.Event
>>>>>>> Stashed changes
import com.shannon.shannonweek14.data.repository.EventRepository
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
                loadPublicEvents()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}