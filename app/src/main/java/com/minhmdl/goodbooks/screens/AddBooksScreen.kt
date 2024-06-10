package com.minhmdl.goodbooks.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.minhmdl.goodbooks.R
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.screens.book.BookViewModel
import com.minhmdl.goodbooks.utils.GoodbooksAlertDialog
import com.minhmdl.goodbooks.utils.GoodbooksDivider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddBooksScreen(
    onOptionSelected: (selected: Boolean, answer: Int) -> Unit,
    onDismiss: () -> Unit,
    book: Book,
    bookViewModel: BookViewModel,
    context: Context
) {
    var otherShelfName by remember { mutableStateOf("") }
    val userId = Firebase.auth.currentUser?.uid ?: ""
    val showDialog = remember { mutableStateOf(false) }
    val possibleAnswers = listOf(
        "Want to Read",
        "Currently Reading",
        "Read"
    )
    val selectedStates = remember { mutableStateListOf(false, false, false) }
    var shelfName1 by remember { mutableStateOf("") }

    LaunchedEffect(key1 = book?.bookID) {
        shelfName1 =
            book?.let { bookViewModel.getShelfName(Firebase.auth.currentUser?.uid, it) }.toString()
    }

    val index = possibleAnswers.indexOf(shelfName1)
    var selectedAnswers = remember { mutableStateOf("Want to Read") }

    if (index != -1) {
        selectedAnswers = remember {
            mutableStateOf(possibleAnswers[index])
        }
        selectedStates[index] = true
    }

    Dialog(onDismissRequest = onDismiss) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.close),
                                tint = MaterialTheme.colors.onSurface
                            )
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(id = R.string.label_add_book),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6
                        )
                    },
                    actions = {

                        IconButton(
                            onClick = {
                                /* TODO: Open search */
                                CoroutineScope(Dispatchers.IO).launch {
                                    bookViewModel.addBook(
                                        userId,
                                        selectedAnswers.value,
                                        book,
                                        context,
                                        shelfExists = { shelfExistsName ->
                                            otherShelfName = shelfExistsName
                                        }
                                    )
                                }
                                onDismiss()
                            },

                            ) {
                            val alpha = ContentAlpha.high
                            CompositionLocalProvider(LocalContentAlpha provides alpha) {
                                Text(
                                    text = stringResource(id = R.string.save),
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.primary
                                )
                            }
                        }
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                )
            },
            modifier = Modifier.height(550.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            ) {
                possibleAnswers.forEachIndexed { index, it ->
                    Log.d("Book Screen", "checkState ${selectedStates[index]}")
                    CheckboxRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = possibleAnswers[index],
                        selected = selectedStates[index],
                        onOptionSelected = { selectedStates[index] = !selectedStates[index] },
                        selectedAnswers = selectedAnswers
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp))
                GoodbooksDivider()
                Text(
                    text = "Remove from My Books",
                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            showDialog.value = true
                        }
                )
                if (showDialog.value) {
                    GoodbooksAlertDialog(
                        openDialog = showDialog.value,
                        title = "Remove book and all related activity?",
                        details = "Removing a book from your shelves deletes any ratings, reviews, or updates you've made associated with this book. This can't be undone.",
                        onDismiss = { showDialog.value = false },
                        onClick = {
                            showDialog.value = false
                            CoroutineScope(Dispatchers.IO).launch {
                                val shelfName = bookViewModel.getShelfName(userId, book)
                                bookViewModel.deleteABookInShelf(
                                    userId,
                                    book,
                                    shelfName = shelfName
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CheckboxRow(
    text: String,
    selected: Boolean,
    onOptionSelected: () -> Unit,
    selectedAnswers: MutableState<String>,
    modifier: Modifier = Modifier
) {
    var checkState by remember { mutableStateOf(selected) }
    LaunchedEffect(selected) {
        checkState = selected
    }
    Surface(
        shape = androidx.compose.material3.MaterialTheme.shapes.small,
        color = if (checkState) {
            androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer
        } else {
            androidx.compose.material3.MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (checkState) {
                androidx.compose.material3.MaterialTheme.colorScheme.primary
            } else {
                androidx.compose.material3.MaterialTheme.colorScheme.outline
            }
        ),
        modifier = modifier
            .clip(androidx.compose.material3.MaterialTheme.shapes.small)
            .clickable(onClick = onOptionSelected)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text,
                Modifier.weight(1f),
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
            )
            Box(Modifier.padding(8.dp)) {
                Checkbox(
                    checked = checkState,
                    onCheckedChange = {
                        checkState = it
                        selectedAnswers.value = text
                    })
            }
        }
    }
}
