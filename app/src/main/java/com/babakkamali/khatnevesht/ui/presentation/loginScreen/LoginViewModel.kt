package com.babakkamali.khatnevesht.ui.presentation.loginScreen

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.babakkamali.khatnevesht.domain.authentication.AuthenticationRepository
import com.babakkamali.khatnevesht.exception.NoConnectivityException
import com.babakkamali.khatnevesht.utils.PreferenceUtils

class LoginViewModel(application: Application) : ViewModel() {

    private val repository = AuthenticationRepository()
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    // LiveData to handle UI states (e.g., loading, error, success)
    val uiState: MutableLiveData<LoginUIState?> = MutableLiveData()

    // Simulated function to send the phone number to the server
    fun sendPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            uiState.value = LoginUIState.Loading
            try {
                val response = repository.loginUser(phoneNumber)
                if (response?.status == "success") {
                    uiState.value = LoginUIState.PhoneNumberSent
                } else {
                    uiState.value = LoginUIState.Error(response?.message ?: "Unknown error")
                }
            } catch (e: NoConnectivityException) {
                // Handle no connectivity exception, e.g. show a user-friendly message
                uiState.value = e.message?.let { LoginUIState.Error(it) }
            } catch (e: Exception) {
                // Handle other exceptions
                uiState.value = e.message?.let { LoginUIState.Error(it) }
            }

        }
    }

    fun verifyToken(phoneNumber: String, token: String) {

        viewModelScope.launch {
            uiState.value = LoginUIState.Loading
            try {
                val response = repository.verifyUser(phoneNumber, token)
                if (response?.status == "success" && PreferenceUtils.saveToken(context,response.data.token)) {
                    uiState.value = LoginUIState.TokenVerified
                } else {
                    uiState.value = LoginUIState.Error(response?.message ?: "Unknown error")
                }
            } catch (e: NoConnectivityException) {
                // Handle no connectivity exception, e.g. show a user-friendly message
                uiState.value = e.message?.let { LoginUIState.Error(it) }
            } catch (e: Exception) {
                // Handle other exceptions
                uiState.value = e.message?.let { LoginUIState.Error(it) }
            }
        }
    }

    fun resetUIState() {
        uiState.value = null

    }


}
class LoginViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
// Sealed class to represent different UI states
sealed class LoginUIState {
    object Loading : LoginUIState()
    object PhoneNumberSent : LoginUIState()
    object TokenVerified : LoginUIState()
    data class Error(val message: String) : LoginUIState()
}
