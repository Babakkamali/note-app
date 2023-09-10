package com.babakkamali.khatnevesht.ui.presentation.updateNoteScreen

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.babakkamali.khatnevesht.R
import com.babakkamali.khatnevesht.data.daos.NoteDao
import com.babakkamali.khatnevesht.data.models.NoteModel
import java.util.Date

@Composable
fun UpdateNoteTopBar(
    viewModel: UpdateNoteViewModel,
    noteId: Int,
    navigateBack: () -> Unit,
    title: String,
    note: String,
) {
    val noteModel = viewModel.noteModel
    CenterAlignedTopAppBar(
        title = { Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.playfair_display_regular)),
            )
        },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(painterResource(R.drawable.ic_baseline_clear_24),
                    contentDescription = "back")
            }
        },
        actions = {
            IconButton(onClick = {
                val updateNote = NoteModel(noteId, title, note, noteModel.createdAt, Date(), noteModel.serverId, noteModel.isDeleted)
                viewModel.updateNotes(updateNote)
                navigateBack()
            }) {
                Icon(painterResource(id = R.drawable.ic_baseline_check_24),
                    contentDescription = "save")
            }
        }
    )
}