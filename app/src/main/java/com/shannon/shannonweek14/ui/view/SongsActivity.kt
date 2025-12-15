package com.shannon.shannonweek14.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shannon.shannonweek14.data.datastore.TokenManager
import com.shannon.shannonweek14.data.model.Songs
import com.shannon.shannonweek14.ui.songs.SongsViewModel
import com.shannon.shannonweek14.ui.theme.Theme

class SongsActivity : ComponentActivity() {

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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                    return@Theme
                }


                if (token == null) {
                    finish()
                    return@Theme
                }


                val viewModel = remember(token) {
                    SongsViewModel(token = "Bearer $token")
                }

                LaunchedEffect(Unit) {
                    viewModel.loadSongs()
                }

                val songs by viewModel.songsList.collectAsState()
                val loading by viewModel.loading.collectAsState()
                val error by viewModel.error.collectAsState()

                var screen by remember { mutableStateOf("start") }
                var score by remember { mutableStateOf(0) }
                var currentIndex by remember { mutableStateOf(0) }
                var selectedAnswer by remember { mutableStateOf<String?>(null) }
                var isCorrect by remember { mutableStateOf<Boolean?>(null) }

                when (screen) {

                    "start" -> SongsStartScreen(
                        onStart = {
                            screen = "question"
                            score = 0
                            currentIndex = 0
                        },
                        onBack = { finish() }
                    )

                    "question" -> {
                        when {
                            loading -> Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }

                            error != null -> Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Error: $error")
                            }

                            songs.isNotEmpty() -> {
                                val question = songs[currentIndex]

                                SongsQuestionScreen(
                                    question = question,
                                    questionIndex = currentIndex + 1,
                                    totalQuestions = songs.size,
                                    selectedAnswer = selectedAnswer,
                                    isCorrect = isCorrect,
                                    onAnswerSelected = { answer ->
                                        selectedAnswer = answer
                                        isCorrect = answer == question.correctAnswer
                                        if (isCorrect == true) score++
                                    },
                                    onNext = {
                                        if (currentIndex < songs.lastIndex) {
                                            currentIndex++
                                            selectedAnswer = null
                                            isCorrect = null
                                        } else {
                                            screen = "result"
                                        }
                                    }
                                )
                            }
                        }
                    }

                    "result" -> SongsResultScreen(
                        score = score,
                        totalQuestions = songs.size,
                        onPlayAgain = {
                            screen = "start"
                            score = 0
                        },
                        onBackHome = { finish() }
                    )
                }
            }
        }
    }
}



@Composable
fun SongsStartScreen(
    onStart: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "🎵 Songs Quiz",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Test how well you know popular songs!",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "🎧",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Start Quiz")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBack) {
            Text("Back to Home")
        }
    }
}



@Composable
fun SongsQuestionScreen(
    question: Songs,
    questionIndex: Int,
    totalQuestions: Int,
    selectedAnswer: String?,
    isCorrect: Boolean?,
    onAnswerSelected: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "🎵 Guess The Song",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Question $questionIndex of $totalQuestions",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Clue", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = question.clue,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        question.options.forEach { option ->
            val color = when {
                selectedAnswer == option && isCorrect == true ->
                    MaterialTheme.colorScheme.primary
                selectedAnswer == option && isCorrect == false ->
                    MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.surface
            }

            Button(
                onClick = { onAnswerSelected(option) },
                enabled = selectedAnswer == null,
                colors = ButtonDefaults.buttonColors(containerColor = color),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Text(option)
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onNext,
            enabled = selectedAnswer != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Next")
        }
    }
}


@Composable
fun SongsResultScreen(
    score: Int,
    totalQuestions: Int,
    onPlayAgain: () -> Unit,
    onBackHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Quiz Completed",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Your Score",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "$score / $totalQuestions",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Well done. Keep refining your taste in music.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onPlayAgain,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Play Again")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Back to Home")
        }
    }
}
