package com.shannon.shannonweek14.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shannon.shannonweek14.dto.ActivityResponseData
import com.shannon.shannonweek14.dto.PetResponseData
import com.shannon.shannonweek14.repository.PetRepository
import com.shannon.shannonweek14.service.ApiClient
import com.shannon.shannonweek14.service.PetService
import com.shannon.shannonweek14.ui.model.Pet
import com.shannon.shannonweek14.ui.model.PetActivityModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// --- State --- //
data class PetUiState(
    val petStatus: Pet? = null,
    val activityHistory: List<PetActivityModel> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,

    // State for Create Pet Card
    val createPetName: String = "",

    // State for Log Activity Card
    val logActivityDescription: String = "",
    val logActivityType: String = "WORK_TASK",

    // State for Update Pet Name Dialog
    val isUpdateDialogVisible: Boolean = false,
    val updatePetNameInput: String = ""
)

// --- Mapper Functions --- //
private fun PetResponseData.toPet(): Pet = Pet(
    id = this.id,
    name = this.name,
    healthScore = this.healthScore,
    happinessScore = this.happinessScore,
    visualState = this.visualState
)

private fun ActivityResponseData.toPetActivityModel(): PetActivityModel = PetActivityModel(
    id = this.id,
    activityType = this.activityType,
    description = this.description,
    createdAt = this.createdAt
)

class PetViewModel(private val token: String) : ViewModel() {

    private val petRepository: PetRepository by lazy {
        PetRepository(ApiClient.getClient(token).create(PetService::class.java), token)
    }

    private val _uiState = MutableStateFlow(PetUiState())
    val uiState: StateFlow<PetUiState> = _uiState.asStateFlow()

    init {
        getPetStatus()
        getActivityHistory()
    }

    fun onCreatePetNameChange(name: String) {
        _uiState.update { it.copy(createPetName = name) }
    }

    fun onLogActivityDescriptionChange(description: String) {
        _uiState.update { it.copy(logActivityDescription = description) }
    }

    fun onLogActivityTypeChange(type: String) {
        _uiState.update { it.copy(logActivityType = type) }
    }

    fun onUpdateDialogNameChange(name: String) {
        _uiState.update { it.copy(updatePetNameInput = name) }
    }

    fun openUpdateDialog() {
        _uiState.update {
            it.copy(isUpdateDialogVisible = true, updatePetNameInput = it.petStatus?.name ?: "")
        }
    }

    fun dismissUpdateDialog() {
        _uiState.update { it.copy(isUpdateDialogVisible = false) }
    }

    fun createPet() = viewModelScope.launch {
        _uiState.update { it.copy(loading = true) }
        try {
            val response = petRepository.createPet(_uiState.value.createPetName)
            if (response.isSuccessful) {
                _uiState.update { it.copy(createPetName = "") } // Reset input
                getPetStatus() // Refresh data
            } else {
                _uiState.update { it.copy(error = "Error: ${response.code()} - ${response.message()}") }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        } finally {
            _uiState.update { it.copy(loading = false) }
        }
    }

    fun updatePetName() = viewModelScope.launch {
        _uiState.update { it.copy(loading = true) }
        try {
            val response = petRepository.updatePetName(_uiState.value.updatePetNameInput)
            if (response.isSuccessful) {
                dismissUpdateDialog()
                getPetStatus()
            } else {
                 _uiState.update { it.copy(error = "Error: ${response.code()} - ${response.message()}") }
            }
        } catch (e: Exception) {
             _uiState.update { it.copy(error = e.message) }
        } finally {
             _uiState.update { it.copy(loading = false) }
        }
    }

    fun logActivity() = viewModelScope.launch {
        _uiState.update { it.copy(loading = true) }
        try {
            val response = petRepository.logActivity(_uiState.value.logActivityType, _uiState.value.logActivityDescription)
            if (response.isSuccessful) {
                _uiState.update { it.copy(logActivityDescription = "") }
                getPetStatus()
                getActivityHistory()
            } else {
                _uiState.update { it.copy(error = "Error: ${response.code()} - ${response.message()}") }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        } finally {
            _uiState.update { it.copy(loading = false) }
        }
    }

    private fun getPetStatus() = viewModelScope.launch {
        _uiState.update { it.copy(loading = true) }
        try {
            val response = petRepository.getPetStatus()
            if (response.isSuccessful) {
                _uiState.update { it.copy(petStatus = response.body()?.data?.toPet(), error = null) }
            } else {
                _uiState.update { it.copy(petStatus = null, error = "Error: ${response.code()} - ${response.message()}") }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(petStatus = null, error = e.message) }
        } finally {
            _uiState.update { it.copy(loading = false) }
        }
    }

    private fun getActivityHistory() = viewModelScope.launch {
        try {
            val response = petRepository.getActivityHistory()
            if (response.isSuccessful) {
                _uiState.update { it.copy(activityHistory = response.body()?.data?.map { it.toPetActivityModel() } ?: emptyList()) }
            } else {
                 _uiState.update { it.copy(error = "Error: ${response.code()} - ${response.message()}") }
            }
        } catch (e: Exception) {
             _uiState.update { it.copy(error = e.message) }
        }
    }
}
