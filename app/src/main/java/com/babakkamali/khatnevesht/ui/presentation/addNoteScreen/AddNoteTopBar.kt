package com.babakkamali.khatnevesht.ui.presentation.addNoteScreen

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.babakkamali.khatnevesht.data.models.NoteModel
import com.babakkamali.khatnevesht.R
import java.util.Date


@Composable
fun AddNoteTopBar(
    viewModel: AddNoteViewModel,
    navigateBack: () -> Unit,
    title: String,
    notes: String,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                stringResource(R.string.note),
                fontFamily = FontFamily(Font(R.font.playfair_display_regular)),
            )
        },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(painterResource(R.drawable.ic_baseline_clear_24),
                    contentDescription = "logo")
            }
        },
        actions = {
            IconButton(onClick = {
                if (title.isNotEmpty() || notes.isNotEmpty()){
                    val currentTime = Date()
                    val noteModel = NoteModel(id = 0, title = title, notes = notes,currentTime,currentTime)
                    viewModel.insertNote(noteModel)
                    navigateBack()
                }else{
                    navigateBack()
                }
            }) {
                Icon(painterResource(id = R.drawable.ic_baseline_check_24),
                    contentDescription = "Git")
            }
        }
    )
}