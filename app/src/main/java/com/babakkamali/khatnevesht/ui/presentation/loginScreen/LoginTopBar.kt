package com.babakkamali.khatnevesht.ui.presentation.loginScreen

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.babakkamali.khatnevesht.R

@Composable
fun LoginTopBar(
    navigateBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Login",
                fontFamily = FontFamily(Font(R.font.playfair_display_regular))
            )
        }
    )
}