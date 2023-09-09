package com.babakkamali.khatnevesht.ui.presentation.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.babakkamali.khatnevesht.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun HomeTopBar(
    navigateToAboutScreen:()-> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(R.string.app_name),
                fontFamily = FontFamily(Font(R.font.playfair_display_regular)),
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.note_logo_2),
                contentDescription = "logo",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
        },
        actions = {
            IconButton(onClick = {navigateToAboutScreen() }) {
                Icon(painterResource(
                    id = R.drawable.about),
                    contentDescription = "about")
            }
        }
    )
}