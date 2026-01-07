package com.shannon.shannonweek14.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.shannon.shannonweek14.R
import com.shannon.shannonweek14.data.datastore.TokenManager
import com.shannon.shannonweek14.ui.model.Pet
import com.shannon.shannonweek14.ui.model.PetActivityModel
import com.shannon.shannonweek14.ui.viewmodel.PetUiState
import com.shannon.shannonweek14.ui.viewmodel.PetViewModel
import com.shannon.shannonweek14.ui.theme.Theme

class PetActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Theme {
                val tokenManager = remember { TokenManager(this) }
                var token by remember { mutableStateOf<String?>(null) }
                var loadingToken by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    token = tokenManager.getToken()
                    loadingToken = false
                }

                if (loadingToken) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                    return@Theme
                }

                if (token == null) {
                    finish()
                    return@Theme
                }

                val viewModel = remember(token) { PetViewModel(token = "Bearer $token") }
                PetScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetScreen(viewModel: PetViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isUpdateDialogVisible) {
        UpdatePetNameDialog(
            currentName = uiState.petStatus?.name ?: "",
            newName = uiState.updatePetNameInput,
            onNameChange = viewModel::onUpdateDialogNameChange,
            onConfirm = viewModel::updatePetName,
            onDismiss = viewModel::dismissUpdateDialog
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Virtual Pet") }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.loading) {
                CircularProgressIndicator()
            }

            uiState.error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 16.dp))
            }

            if (uiState.petStatus == null && !uiState.loading) {
                CreatePetCard(
                    petName = uiState.createPetName,
                    onNameChange = viewModel::onCreatePetNameChange,
                    onCreateClick = viewModel::createPet
                )
            } else {
                uiState.petStatus?.let { pet ->
                    PetStatusCard(pet = pet, onEditClick = viewModel::openUpdateDialog)
                    Spacer(modifier = Modifier.height(16.dp))
                    LogActivityCard(
                        description = uiState.logActivityDescription,
                        onDescriptionChange = viewModel::onLogActivityDescriptionChange,
                        selectedActivity = uiState.logActivityType,
                        onActivitySelect = viewModel::onLogActivityTypeChange,
                        onLogClick = viewModel::logActivity
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ActivityHistoryList(activities = uiState.activityHistory)
                }
            }
        }
    }
}

@Composable
fun CreatePetCard(petName: String, onNameChange: (String) -> Unit, onCreateClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Create Your Pet", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = petName, onValueChange = onNameChange, label = { Text("Pet Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onCreateClick, enabled = petName.isNotBlank()) {
                Text("Create Pet")
            }
        }
    }
}

@Composable
fun PetStatusCard(pet: Pet, onEditClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val petImageRes = when (pet.visualState) {
                "SICK" -> R.drawable.sick_cat
                "SAD" -> R.drawable.sad_cat
                "THIN" -> R.drawable.cat_thin
                else -> R.drawable.happy_cat
            }

            Image(
                painter = painterResource(id = petImageRes),
                contentDescription = pet.visualState, // Accessibility
                modifier = Modifier.size(120.dp).padding(bottom = 16.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = pet.name, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Name")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(progress = pet.healthScore / 100f, modifier = Modifier.fillMaxWidth())
            Text("Health: ${pet.healthScore}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(progress = pet.happinessScore / 100f, modifier = Modifier.fillMaxWidth())
            Text("Happiness: ${pet.happinessScore}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Status: ${pet.visualState}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun LogActivityCard(
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedActivity: String,
    onActivitySelect: (String) -> Unit,
    onLogClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Log New Activity", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = description, onValueChange = onDescriptionChange, label = { Text("Activity Description") }, modifier = Modifier.fillMaxWidth())
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = selectedActivity == "WORK_TASK", onClick = { onActivitySelect("WORK_TASK") })
                Text("Work/Task")
                Spacer(Modifier.width(16.dp))
                RadioButton(selected = selectedActivity == "REST_MEANINGFUL", onClick = { onActivitySelect("REST_MEANINGFUL") })
                Text("Meaningful Rest")
            }
            Button(onClick = onLogClick, enabled = description.isNotBlank()) {
                Text("Log Activity")
            }
        }
    }
}

@Composable
fun ActivityHistoryList(activities: List<PetActivityModel>) {
    Text("Activity History", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn {
        items(activities) { activity ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = activity.activityType, style = MaterialTheme.typography.bodyLarge)
                    Text(text = activity.description, style = MaterialTheme.typography.bodyMedium)
                    Text(text = activity.createdAt, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun UpdatePetNameDialog(
    currentName: String,
    newName: String,
    onNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Pet Name") },
        text = {
            OutlinedTextField(
                value = newName,
                onValueChange = onNameChange,
                label = { Text("New Name") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = onConfirm, enabled = newName.isNotBlank() && newName != currentName) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

