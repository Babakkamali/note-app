package com.babakkamali.khatnevesht

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.babakkamali.khatnevesht.ui.KhatNeveshtApp
import com.babakkamali.khatnevesht.ui.KhatNeveshtAppScreens
import com.babakkamali.khatnevesht.ui.theme.KhatneveshtTheme
import com.babakkamali.khatnevesht.utils.ContextProvider

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val logoutReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            navController.navigate(KhatNeveshtAppScreens.Login.name) {
                popUpTo(KhatNeveshtAppScreens.Home.name) { inclusive = true }
            }
            Toast.makeText(applicationContext, getString(R.string.session_expired),Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextProvider.init(applicationContext)
        setContent {
            KhatneveshtTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }

    @Composable
    fun MyApp() {
        navController = rememberNavController()
        KhatNeveshtApp(navController = navController)
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()
        registerReceiver(logoutReceiver, IntentFilter("ACTION_LOGOUT"))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(logoutReceiver)
    }
}