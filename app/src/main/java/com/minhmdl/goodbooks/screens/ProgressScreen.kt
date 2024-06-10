package com.minhmdl.goodbooks.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.minhmdl.goodbooks.R
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.screens.book.BookViewModel
import com.minhmdl.goodbooks.utils.GoodbooksAlertDialog
import com.minhmdl.goodbooks.utils.GoodbooksDivider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProgressScreen(
    onOptionSelected: (selected: Boolean, answer: Int) -> Unit,
    onDismiss: () -> Unit,
    book: Book,
    bookViewModel: BookViewModel,
    context: Context
) {
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
                            text = stringResource(id = R.string.label_progress),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                /* TODO: Open search */
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
            }
        ) {innerPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {

                Text(
                    text = "On Page",
                    color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )


            }
        }
    }
}
