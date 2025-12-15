package com.shannon.shannonweek14.ui.view

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.LaunchedEffect
import com.shannon.shannonweek14.ui.view.HomeActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shannon.shannonweek14.ui.theme.Theme
import com.shannon.shannonweek14.data.datastore.TokenManager
import com.shannon.shannonweek14.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Theme {
                val authViewModel: AuthViewModel = viewModel()
                val context = LocalContext.current

                val tokenManager = remember { TokenManager(context) }
                val scope = rememberCoroutineScope()

                val loginToken by authViewModel.loginResponse.observeAsState()

                LaunchedEffect(loginToken) {
                    if (loginToken != null) {
                        tokenManager.saveToken(loginToken!!)
                        Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(context, HomeActivity::class.java)
                        context.startActivity(intent)

                        (context as? ComponentActivity)?.finish()
                    }
                }

                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                var error by remember { mutableStateOf<String?>(null) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Login", style = MaterialTheme.typography.titleLarge)

                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val cleanEmail = email.trim()
                            val cleanPassword = password.trim()

                            if (cleanEmail.isNotEmpty() && cleanPassword.isNotEmpty()) {
                                authViewModel.login(cleanEmail, cleanPassword)

                            } else {
                                Toast.makeText(
                                    context,
                                    "Email dan Password tidak boleh kosong!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        Text("Login")
                    }

                    authViewModel.error.value?.let { errorMessage ->
                        Spacer(Modifier.height(12.dp))
                        Text(errorMessage, color = MaterialTheme.colorScheme.error)
                    }

                }
            }
        }
    }
}