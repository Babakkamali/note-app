package com.babakkamali.khatnevesht.ui

sealed class KhatNeveshtAppScreens(val name : String){
    object Home : KhatNeveshtAppScreens("home")
    object AddNotes : KhatNeveshtAppScreens("add_note")
    object UpdateNotes : KhatNeveshtAppScreens("update_note/{id}")
    object About:KhatNeveshtAppScreens("about")
    object Login : KhatNeveshtAppScreens("login")
}