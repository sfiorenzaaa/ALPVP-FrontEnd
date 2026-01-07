package com.shannon.shannonweek14.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shannon.shannonweek14.data.datastore.TokenManager
import com.shannon.shannonweek14.ui.model.Event
import com.shannon.shannonweek14.ui.theme.Theme
import com.shannon.shannonweek14.ui.viewmodel.EventViewModel
import com.shannon.shannonweek14.dto.EventResponse
import kotlinx.coroutines.flow.first

import kotlinx.coroutines.runBlocking

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

        val tokenManager = TokenManager(applicationContext)
        val (token, role) = runBlocking {
            val t = tokenManager.getToken() ?: ""
            val r = tokenManager.role.first() ?: "USER" // Default ke USER
            Pair(t, r)
        }

        val isAdmin = role == "ADMIN"

        setContent {
            Theme {
                val viewModel: EventViewModel = viewModel(
                    factory = EventViewModelFactory(token)
                )

                EventScreen(viewModel, isAdmin)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(viewModel: EventViewModel, isAdmin : Boolean) {
    // State
    val publicEvents by viewModel.publicEvents.collectAsState()
    val myEvents by viewModel.myEvents.collectAsState()
    val pendingEvents by viewModel.pendingEvents.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    var showCreateDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current



    LaunchedEffect(Unit) {
        viewModel.loadPublicEvents()
        viewModel.loadMyEvents()
        if(isAdmin){
            viewModel.loadPendingEvents()
        }
    }

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
            if (selectedTabIndex != 2){
                FloatingActionButton(onClick = { showCreateDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Event")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            val tabs = if (isAdmin) listOf("Public Events", "My Events", "Pending Events") else  listOf("Public Events", "My Events")
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
                }
            }

            val eventsToShow = if (isAdmin) {
                when (selectedTabIndex) {
                    0 -> publicEvents
                    1 -> myEvents
                    2 -> pendingEvents
                    else -> emptyList()
                }
            } else {
                when (selectedTabIndex) {
                    0 -> publicEvents
                    1 -> myEvents
                    else -> emptyList()
                }
            }


            // content list
            if (loading) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (eventsToShow.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Text("No events found.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // Ini memaksa LazyColumn ambil SEMUA sisa ruang ke bawah
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(eventsToShow) { event ->
                        EventItem(
                            event = event,
                            isAdminTab = (selectedTabIndex == 2),
                            onJoinClick = {
                                viewModel.joinEvent(event.id)
                                Toast.makeText(context, "Processing...", Toast.LENGTH_SHORT).show()
                            },
                            onApprove = { viewModel.approveEvent(event.id) },
                            onReject = { viewModel.rejectEvent(event.id) }
                        )
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateEventDialog(
            onDismiss = { showCreateDialog = false },
            onSubmit = { t, d, dt ->
                viewModel.createEvent(t, d, dt) { showCreateDialog = false }
            }
        )
    }
}

@Composable
fun EventItem(
    // 1. Parameter disesuaikan dengan error log kamu
    event: Event,
    isAdminTab: Boolean = false,
    onJoinClick: () -> Unit = {},
    onApprove: () -> Unit = {},
    onReject: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // --- JUDUL & STATUS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.title ?: "No Title",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Surface(
                    color = if (event.status == "APPROVE") Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = event.status ?: "NULL",
                        color = if (event.status == "APPROVE") Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- AUTHOR & DESKRIPSI ---
            Text(
                text = "By: ${event.author ?: "Unknown"}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = event.description ?: "-",
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // --- TANGGAL ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = event.date ?: "No Date", fontSize = 12.sp)
            }

            if (isAdminTab) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Approve", color = Color.White)
                    }
                    Button(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reject", color = Color.White)
                    }
                }
            } else {
                if (event.status == "PENDING") {
                    OutlinedButton(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Color(0xFFEF6C00))
                    ) {
                        Text("Waiting for Admin Approval", color = Color(0xFFEF6C00))
                    }
                } else if (event.status == "REJECT") {
                    OutlinedButton(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Color.Red)
                    ) {
                        Text("Event Rejected by Admin", color = Color.Red)
                    }
                } else {
                    if (event.isJoined) {
                        Button(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(disabledContainerColor = Color(0xFFA5D6A7), disabledContentColor = Color(0xFF1B5E20))
                        ) {
                            Icon(Icons.Default.Check, null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Joined")
                        }
                    } else {
                        Button(
                            onClick = onJoinClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0B0))
                        ) {
                            Text("Join Event", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun StatusChip(status: String) {
    val (bgColor, textColor) = when (status) {
        "APPROVE" -> Color(0xFFE7F6E7) to Color(0xFF2E7D32) // Hijau untuk Approve
        "REJECT" -> Color(0xFFFFEBEE) to Color(0xFFC62828)  // Merah untuk Reject
        else -> Color(0xFFFFF8E1) to Color(0xFFF57C00)      // Kuning untuk Pending
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
        )
    }
}

@Composable
fun CreateEventDialog(onDismiss: () -> Unit, onSubmit: (String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("2025-12-31") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Event") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    placeholder = { Text("2025-01-01") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()) {
                    onSubmit(title, description, "${date}T00:00:00Z")
                }
            }) {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EventScreenPreview() {
    // KITA BIKIN DATA PALSU YANG BANYAK (20 ITEM) SUPAYA PASTI SCROLLING
    val dummyEvents = List(20) { index ->
        Event(
            id = index,
            title = "Event Lari Marathon ke-$index",
            description = "Lari keliling komplek sambil bawa beban kenangan masa lalu yang ke-$index.",
            date = "2025-10-${index + 1}",
            status = if (index % 2 == 0) "APPROVE" else "PENDING",
            author = "User $index",
            isJoined = index % 3 == 0,
            eventDate = "2025-12-31T00:00:00Z", // Isi string tanggal sembarang
            createdAt = "2025-01-01T10:00:00Z", // Isi string tanggal sembarang
            userId = 100 + index
        )
    }

    var selectedTab by remember { mutableStateOf(0) }

    MaterialTheme {
        EventScreenContent(
            events = dummyEvents,
            isLoading = false,
            selectedTabIndex = selectedTab,
            isAdmin = true, // Coba ganti false buat test tampilan user biasa
            onTabSelected = { selectedTab = it },
            onFabClick = {},
            onJoinClick = {},
            onApprove = {},
            onReject = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreenContent(
    events: List<Event>,
    isLoading: Boolean,
    selectedTabIndex: Int,
    isAdmin: Boolean,
    onTabSelected: (Int) -> Unit,
    onFabClick: () -> Unit,
    onJoinClick: (Int) -> Unit,
    onApprove: (Int) -> Unit,
    onReject: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Community Events (Preview)") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            if (selectedTabIndex != 2) {
                FloatingActionButton(onClick = onFabClick) {
                    Icon(Icons.Default.Add, contentDescription = "Add Event")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // TAB ROW
            val tabs = if (isAdmin) {
                listOf("Public Events", "My Events", "Pending Events")
            } else {
                listOf("Public Events", "My Events")
            }

            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { onTabSelected(index) },
                        text = { Text(title) }
                    )
                }
            }



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // <--- INI KUNCINYA. Dia ambil semua sisa tinggi layar.
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    if (events.isEmpty()) {
                        Text(
                            text = "No events found.",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize() // LazyColumn isi penuh Box-nya
                        ) {
                            items(events) { event ->
                                EventItemPreviewOnly(
                                    event = event,
                                    isAdminTab = (selectedTabIndex == 2),
                                    onJoinClick = { onJoinClick(event.id) },
                                    onApprove = { onApprove(event.id) },
                                    onReject = { onReject(event.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 2. ITEM COMPOSABLE (Copy dari kodemu, disesuaikan dikit buat preview) ---
@Composable
fun EventItemPreviewOnly(
    event: Event,
    isAdminTab: Boolean,
    onJoinClick: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(event.title ?: "-", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Surface(
                    color = if (event.status == "APPROVE") Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = event.status ?: "-",
                        color = if (event.status == "APPROVE") Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("By: ${event.author}", fontSize = 12.sp, color = Color.Gray)
            Text(event.description ?: "-", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, null, tint = Color.Red, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(event.date ?: "-", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Tombol-tombol
            if (isAdminTab) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onApprove, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))) { Text("Approve") }
                    Button(onClick = onReject, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(Color(0xFFF44336))) { Text("Reject") }
                }
            } else {
                if (event.isJoined) {
                    Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Check, null)
                        Text("Joined")
                    }
                } else {
                    Button(onClick = onJoinClick, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Color(0xFFE0E0B0))) {
                        Text("Join Event", color = Color.Black)
                    }
                }
            }
        }
    }
}
