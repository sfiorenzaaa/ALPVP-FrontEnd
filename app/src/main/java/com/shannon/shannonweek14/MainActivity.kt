package com.shannon.shannonweek14

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shannon.shannonweek14.ui.theme.Theme
import com.shannon.shannonweek14.ui.view.JournalActivity
import com.shannon.shannonweek14.ui.view.SongsActivity
import com.shannon.shannonweek14.ui.view.GuessEmojiActivity
import com.shannon.shannonweek14.ui.view.PetActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Theme {
                HomeMenuScreen(
                    onSongsClick = {
                        startActivity(Intent(this, SongsActivity::class.java))
                    },
                    onJournalClick = {
                        startActivity(Intent(this, JournalActivity::class.java))
                    },
                    onGuessClick = {
                        startActivity(Intent(this, GuessEmojiActivity::class.java))
                    },
                    onPetClick = {
                        startActivity(Intent(this, PetActivity::class.java))
                    }
                )
            }
        }
    }
}

@Composable
fun HomeMenuScreen(
    onSongsClick: () -> Unit,
    onJournalClick: () -> Unit,
    onGuessClick: () -> Unit,
    onPetClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MenuCard(title = "🎵  Songs Quiz", subtitle = "Small fun quiz guessing song lyrics.") {
            onSongsClick()
        }

        Spacer(modifier = Modifier.height(16.dp))

        MenuCard(title = "📓  Journal", subtitle = "Write and save your daily thoughts.") {
            onJournalClick()
        }

        Spacer(modifier = Modifier.height(16.dp))

        MenuCard(title = "🤔  Guess The Emoji", subtitle = "Decode the emoji puzzle!") {
            onGuessClick()
        }

        Spacer(modifier = Modifier.height(16.dp))

        MenuCard(title = "🐾  Virtual Pet", subtitle = "Take care of your own virtual pet.") {
            onPetClick()
        }
    }
}

@Composable
fun MenuCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
