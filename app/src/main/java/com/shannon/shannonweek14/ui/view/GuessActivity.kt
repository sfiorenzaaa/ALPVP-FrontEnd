package com.shannon.shannonweek14.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shannon.shannonweek14.ui.theme.ColorPalette
import com.shannon.shannonweek14.ui.theme.Theme
import com.shannon.shannonweek14.ui.viewmodel.EmojiQuizViewModel

class GuessEmojiActivity : ComponentActivity() {

    private val vm = EmojiQuizViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                if (vm.showResult.value) {
                    EmojiResultScreen(vm)
                } else {
                    EmojiQuizScreen(vm)
                }
            }
        }
    }
}

@Composable
fun EmojiQuizScreen(vm: EmojiQuizViewModel) {
    val quiz = vm.quizList[vm.currentIndex.value]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Guess The Emoji",
            style = MaterialTheme.typography.titleLarge,
            color = ColorPalette.textPrimary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = ColorPalette.background
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = quiz.emoji,
                    style = MaterialTheme.typography.displayLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "What word does this represent?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ColorPalette.textPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        quiz.options.forEach { option ->
            val isSelected = vm.selectedAnswer.value == option

            val background = when {
                isSelected && option == quiz.correctAnswer ->
                    ColorPalette.primary
                isSelected && option != quiz.correctAnswer ->
                    ColorPalette.secondary.copy(alpha = 0.6f)
                else ->
                    ColorPalette.background
            }

            val textColor = when {
                isSelected -> Color.White
                else -> ColorPalette.textPrimary
            }

            Button(
                onClick = {
                    if (vm.selectedAnswer.value == null) {
                        vm.selectAnswer(option)
                    }
                },
                enabled = vm.selectedAnswer.value == null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = background,
                    contentColor = textColor,
                    disabledContainerColor = background,
                    disabledContentColor = textColor
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Text(option)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { vm.nextQuestion() },
            enabled = vm.selectedAnswer.value != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorPalette.primary,
                contentColor = Color.White,
                disabledContainerColor = ColorPalette.primary.copy(alpha = 0.4f),
                disabledContentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Next")
        }
    }
}

@Composable
fun EmojiResultScreen(vm: EmojiQuizViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Challenge Completed",
            style = MaterialTheme.typography.titleLarge,
            color = ColorPalette.textPrimary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Your Score",
            style = MaterialTheme.typography.bodyMedium,
            color = ColorPalette.textPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "${vm.score.value} / ${vm.quizList.size}",
            style = MaterialTheme.typography.displayLarge,
            color = ColorPalette.textPrimary
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { vm.resetQuiz() },
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorPalette.primary,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Play Again")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { /* Back to Home */ },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = ColorPalette.textPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Back to Home")
        }
    }
}
