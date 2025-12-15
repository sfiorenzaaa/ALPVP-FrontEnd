package com.shannon.shannonweek14.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shannon.shannonweek14.ui.theme.ColorPalette
import com.shannon.shannonweek14.ui.theme.Theme

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                HomeScreen()
            }
        }
    }

    @Composable
    fun HomeScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "What do you want to do today?",
                style = MaterialTheme.typography.titleLarge,
                color = ColorPalette.textPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            CardMenu(
                title = "🎵 Songs Quiz",
                description = "Small fun quiz guessing song lyrics."
            ) {
                startActivity(Intent(this@HomeActivity, SongsActivity::class.java))
            }

            Spacer(modifier = Modifier.height(20.dp))

            CardMenu(
                title = "📓 Journal",
                description = "Write and save your daily thoughts."
            ) {
                startActivity(Intent(this@HomeActivity, JournalActivity::class.java))
            }

            Spacer(modifier = Modifier.height(20.dp))

            CardMenu(
                title = "🤔 Guess The Emoji",
                description = "Decode the emoji puzzle!"
            ) {
                startActivity(Intent(this@HomeActivity, GuessEmojiActivity::class.java))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // TAMBAHKAN INI - EVENT CARD
            CardMenu(
                title = "📅 Events",
                description = "Browse and create community events."
            ) {
                startActivity(Intent(this@HomeActivity, EventActivity::class.java))
            }

            Spacer(modifier = Modifier.height(80.dp))

            CardMenu(
                title = "📅 Events",
                description = "Browse and create community events."
            ) {
                startActivity(Intent(this@HomeActivity, EventActivity::class.java))
            }

        }
    }

    @Composable
    fun CardMenu(
        title: String,
        description: String,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = ColorPalette.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = ColorPalette.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ColorPalette.textPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
