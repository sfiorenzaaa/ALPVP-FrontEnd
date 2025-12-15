package com.shannon.shannonweek14.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shannon.shannonweek14.data.datastore.TokenManager
import com.shannon.shannonweek14.ui.theme.ColorPalette
import com.shannon.shannonweek14.ui.theme.Theme
import com.shannon.shannonweek14.ui.viewmodel.JournalViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.shannon.shannonweek14.ui.model.Journal

class JournalActivity : ComponentActivity() {

    private lateinit var vm: JournalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenManager = TokenManager(this)

        lifecycleScope.launch {
            val token = tokenManager.getToken() ?: ""
            vm = JournalViewModel(token)
            vm.loadJournals()

            setContent {
                Theme {
                    JournalMainScreen(
                        vm = vm,
                        onBackHome = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun JournalMainScreen(
    vm: JournalViewModel,
    onBackHome: () -> Unit
) {
    var newEntry by remember { mutableStateOf("") }
    var showHistory by remember { mutableStateOf(false) }

    val journals by vm.journals.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    if (showHistory) {
        JournalHistoryScreen(journals) {
            showHistory = false
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Journal",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Write your thoughts freely.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = ColorPalette.background
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    OutlinedTextField(
                        value = newEntry,
                        onValueChange = { newEntry = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        placeholder = { Text("What’s on your mind today?") },
                        maxLines = 10
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (newEntry.isNotBlank()) {
                                vm.createJournal(newEntry)
                                newEntry = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Save Entry")
                    }
                }
            }

            if (loading) {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = { showHistory = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("View Previous Notes")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onBackHome,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Back to Home")
            }
        }
    }
}


@Composable
fun JournalHistoryScreen(
    journals: List<Journal>,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Your Past Reflections",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (journals.isEmpty()) {
            Text(
                text = "No journal entries yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            journals.reversed().forEach { journal ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ColorPalette.background
                    )
                ) {
                    Text(
                        text = journal.content,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Back")
        }
    }
}
