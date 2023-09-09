package com.babakkamali.khatnevesht

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.babakkamali.khatnevesht.ui.KhatNeveshtApp
import com.babakkamali.khatnevesht.ui.presentation.homeScreen.HomeViewModel
import com.babakkamali.khatnevesht.ui.theme.KhatneveshtTheme
import com.babakkamali.khatnevesht.utils.ContextProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
            ContextProvider.init(this)
            KhatneveshtTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    KhatNeveshtApp()
                }
            }
        }
    }
}