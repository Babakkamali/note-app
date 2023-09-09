package com.babakkamali.khatnevesht.ui.presentation.loginScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babakkamali.khatnevesht.ui.KhatNeveshtAppScreens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onPhoneNumberEntered: (phoneNumber: String) -> Unit,
    onTokenEntered: (token: String) -> Unit,
    navigateToHomeScreen: () -> Unit,
    viewModel: LoginViewModel
) {
    val phoneNumberState = remember { mutableStateOf("") }
    val tokenState = remember { mutableStateOf("") }
    val isPhoneNumberEntered = remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.observeAsState()
    val context = LocalContext.current
    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUIState.TokenVerified -> {
                navigateToHomeScreen()
                viewModel.resetUIState()
            }

        }
    }

    Scaffold(
        topBar = {},
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                when (uiState) {
                    is LoginUIState.PhoneNumberSent -> {
                        isPhoneNumberEntered.value = true
                    }
                    is LoginUIState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is LoginUIState.Error -> {
                        Toast.makeText(context, (uiState as LoginUIState.Error).message, Toast.LENGTH_SHORT).show()
                    }
                }
                if (!isPhoneNumberEntered.value) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = phoneNumberState.value,
                            onValueChange = {
                                if (it.length <= 9) phoneNumberState.value = it
                            },
                            label = {
                                Text(
                                    "Phone Number",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            leadingIcon = {
                                Text(
                                    text = "09",
                                    modifier = Modifier.padding(start = 32.dp, bottom = 2.dp),
                                    fontSize = 24.sp
                                )
                            },
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 24.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            viewModel.sendPhoneNumber("09" + phoneNumberState.value)  // Call the method in the ViewModel
                        }) {
                            Text("Send SMS Token")
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = tokenState.value,
                            onValueChange = {
                                if (it.length <= 6) tokenState.value = it
                            },
                            label = { Text("Token", color = MaterialTheme.colorScheme.onSurface) },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            Log.e("MyTag","Login Button clicked")
                            viewModel.verifyToken(
                                "09" + phoneNumberState.value,
                                tokenState.value
                            )  // Call the method in the ViewModel
                        }) {
                            Text("Login")
                        }
                    }
                }
            }
        }
    )
}