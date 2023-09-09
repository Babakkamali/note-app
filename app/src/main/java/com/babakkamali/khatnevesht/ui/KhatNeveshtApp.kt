package com.babakkamali.khatnevesht.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType.Companion.IntType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.babakkamali.khatnevesht.ui.presentation.aboutScreen.AboutScreen
import com.babakkamali.khatnevesht.ui.presentation.addNoteScreen.AddNoteScreen
import com.babakkamali.khatnevesht.ui.presentation.homeScreen.HomeScreen
import com.babakkamali.khatnevesht.ui.presentation.loginScreen.LoginScreen
import com.babakkamali.khatnevesht.ui.presentation.loginScreen.LoginViewModel
import com.babakkamali.khatnevesht.ui.presentation.loginScreen.LoginViewModelFactory
import com.babakkamali.khatnevesht.ui.presentation.updateNoteScreen.UpdateNoteScreen
import com.babakkamali.khatnevesht.utils.PreferenceUtils

@Composable
fun KhatNeveshtApp(
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("auth_token", null)
    val startDestination = if (token == null) KhatNeveshtAppScreens.Login.name else KhatNeveshtAppScreens.Home.name
    val application = LocalContext.current.applicationContext as Application
    val factory = LoginViewModelFactory(application)
    val loginViewModel: LoginViewModel = viewModel(factory = factory)


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = KhatNeveshtAppScreens.Login.name) {
            LoginScreen(
                viewModel = loginViewModel, // Pass the viewModel here
                onPhoneNumberEntered = { /* handle phone number */ },
                onTokenEntered = { /* handle token */ },
                navigateToHomeScreen = {
                    navController.navigate(KhatNeveshtAppScreens.Home.name) {
                        popUpTo(KhatNeveshtAppScreens.Login.name) { inclusive = true }
                    }
                }
            )
        }
        composable(route = KhatNeveshtAppScreens.Home.name) {
            HomeScreen(
                onFabClicked = { navController.navigate(KhatNeveshtAppScreens.AddNotes.name) },
                navigateToUpdateNoteScreen = { noteId ->
                    navController.navigate("${KhatNeveshtAppScreens.UpdateNotes.name}/$noteId")
                },
                navigateToAboutScreen = { navController.navigate(KhatNeveshtAppScreens.About.name) }
            )
        }
        composable(route = "${KhatNeveshtAppScreens.UpdateNotes.name}/{noteId}",
            arguments = listOf(navArgument("noteId") { type = IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
            UpdateNoteScreen(
                noteId = noteId,
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(KhatNeveshtAppScreens.AddNotes.name) {
            AddNoteScreen(navigateBack = { navController.popBackStack() })
        }
        composable(KhatNeveshtAppScreens.About.name) {
            AboutScreen(navigateBack = { navController.popBackStack() },
                onLogout = {
                    PreferenceUtils.removeToken(context)
                    navigateToLogin(navController)
                }
            )
        }
    }
}
fun navigateToLogin(navController: NavHostController) {
    navController.navigate(KhatNeveshtAppScreens.Login.name) {
        popUpTo(KhatNeveshtAppScreens.Home.name) { inclusive = true }
    }
}