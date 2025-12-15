package com.shannon.shannonweek14.ui.view

import android.os.Bundle
<<<<<<< Updated upstream
<<<<<<< Updated upstream
import android.widget.Toast
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
<<<<<<< Updated upstream
<<<<<<< Updated upstream
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
=======
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
>>>>>>> Stashed changes
=======
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
>>>>>>> Stashed changes
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
<<<<<<< Updated upstream
<<<<<<< Updated upstream
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shannon.shannonweek14.data.datastore.TokenManager
import com.shannon.shannonweek14.data.model.Event
import com.shannon.shannonweek14.ui.theme.Theme
import com.shannon.shannonweek14.ui.viewmodel.EventViewModel
import kotlinx.coroutines.runBlocking

// 1. FACTORY: Wajib ada karena ViewModel butuh parameter 'token'
class EventViewModelFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class EventActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ambil token secara sinkron untuk inisialisasi awal
        val tokenManager = TokenManager(applicationContext)
        val token = runBlocking {
            tokenManager.getToken() ?: ""
        }

        setContent {
            Theme {
                // Inisialisasi ViewModel menggunakan Factory
                val viewModel: EventViewModel = viewModel(
                    factory = EventViewModelFactory(token)
                )

                EventScreen(viewModel)
=======
=======
>>>>>>> Stashed changes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.shannon.shannonweek14.data.datastore.TokenManager
import com.shannon.shannonweek14.ui.model.Event
import com.shannon.shannonweek14.ui.theme.ColorPalette
import com.shannon.shannonweek14.ui.theme.Theme
import com.shannon.shannonweek14.ui.viewmodel.EventViewModel
import kotlinx.coroutines.launch

class EventActivity : ComponentActivity() {

    private lateinit var vm: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenManager = TokenManager(this)

        lifecycleScope.launch {
            val token = tokenManager.getToken() ?: ""
            vm = EventViewModel(token)
            vm.loadPublicEvents()

            setContent {
                Theme {
                    EventMainScreen(
                        vm = vm,
                        onBackHome = { finish() }
                    )
                }
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
            }
        }
    }
}

<<<<<<< Updated upstream
<<<<<<< Updated upstream
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(viewModel: EventViewModel) {
    // State
    val publicEvents by viewModel.publicEvents.collectAsState()
    val myEvents by viewModel.myEvents.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    var showCreateDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Load data awal
    LaunchedEffect(Unit) {
        viewModel.loadPublicEvents()
        viewModel.loadMyEvents()
    }

    // Tampilkan Toast jika ada error
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Community Events") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // TABS
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Public Events") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("My Events") }
                )
            }

            // CONTENT LIST
            Box(modifier = Modifier.fillMaxSize()) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    val eventsToShow = if (selectedTabIndex == 0) publicEvents else myEvents

                    if (eventsToShow.isEmpty()) {
                        Text(
                            text = "No events found.",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(eventsToShow) { event ->
                                EventItem(event)
                            }
                        }
                    }
                }
            }
        }

        // DIALOG CREATE EVENT
        if (showCreateDialog) {
            CreateEventDialog(
                onDismiss = { showCreateDialog = false },
                onSubmit = { title, desc, date ->
                    viewModel.createEvent(title, desc, date) {
                        showCreateDialog = false
                        Toast.makeText(context, "Event Created!", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

@Composable
fun EventItem(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                StatusChip(status = event.status)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "By: ${event.user?.username ?: "Unknown"}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = event.description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "📅 ${event.eventDate.take(10)}", // Ambil tanggalnya saja (YYYY-MM-DD)
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (bgColor, textColor) = when (status) {
        "APPROVE" -> Color(0xFFE7F6E7) to Color(0xFF2E7D32) // Hijau
        "REJECT" -> Color(0xFFFFEBEE) to Color(0xFFC62828)  // Merah
        else -> Color(0xFFFFF8E1) to Color(0xFFF57C00)      // Kuning (Pending)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
=======
=======
>>>>>>> Stashed changes
@Composable
fun EventMainScreen(
    vm: EventViewModel,
    onBackHome: () -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showMyEvents by remember { mutableStateOf(false) }

    val publicEvents by vm.publicEvents.collectAsState()
    val myEvents by vm.myEvents.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = if (showMyEvents) "My Events" else "Public Events",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    showMyEvents = false
                    vm.loadPublicEvents()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Public")
            }

            Button(
                onClick = {
                    showMyEvents = true
                    vm.loadMyEvents()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("My Events")
            }

            Button(
                onClick = { showCreateDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Text("Create")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val eventsList = if (showMyEvents) myEvents else publicEvents

            if (eventsList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No events found", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(eventsList) { event ->
                        EventCard(event)
                    }
                }
            }
        }

        error?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }

    if (showCreateDialog) {
        CreateEventDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { title, desc, date ->
                vm.createEvent(title, desc, date) {
                    showCreateDialog = false
                }
            }
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
        )
    }
}

@Composable
<<<<<<< Updated upstream
<<<<<<< Updated upstream
fun CreateEventDialog(onDismiss: () -> Unit, onSubmit: (String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    // Default date hardcoded dulu untuk kemudahan, nanti bisa pakai DatePicker
    var date by remember { mutableStateOf("2025-12-31") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Event") },
        text = {
            Column {
=======
=======
>>>>>>> Stashed changes
fun EventCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorPalette.background
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                color = ColorPalette.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status: ${event.status}",
                    style = MaterialTheme.typography.bodySmall,
                    color = when (event.status) {
                        "APPROVE" -> Color.Green
                        "REJECT" -> Color.Red
                        else -> Color.Gray
                    }
                )

                event.user?.let {
                    Text(
                        text = "By: ${it.username}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun CreateEventDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("2025-12-31T10:00:00Z") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Event") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
<<<<<<< Updated upstream
<<<<<<< Updated upstream
                Spacer(modifier = Modifier.height(8.dp))
=======

                Spacer(modifier = Modifier.height(8.dp))

>>>>>>> Stashed changes
=======

                Spacer(modifier = Modifier.height(8.dp))

>>>>>>> Stashed changes
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
<<<<<<< Updated upstream
<<<<<<< Updated upstream
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    placeholder = { Text("2025-01-01") },
=======
=======
>>>>>>> Stashed changes
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (ISO Format)") },
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
<<<<<<< Updated upstream
<<<<<<< Updated upstream
            Button(onClick = {
                if (title.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()) {
                    onSubmit(title, description, "${date}T00:00:00Z") // Format ISO standar backend
                }
            }) {
                Text("Save")
=======
=======
>>>>>>> Stashed changes
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        onConfirm(title, description, date)
                    }
                }
            ) {
                Text("Create")
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}