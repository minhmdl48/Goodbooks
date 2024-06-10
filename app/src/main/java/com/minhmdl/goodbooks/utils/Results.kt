package com.minhmdl.goodbooks.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.R
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations

@Composable
fun SearchResults(
    navController: NavController,
    searchResults: List<Book>,
    onClick:(String) -> Unit
){
Column {
    Text(
        text = stringResource(R.string.search_count, searchResults.size),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
    )
    LazyColumn {
        itemsIndexed(searchResults) { index, book ->
            book.imageLinks.thumbnail?.let {
                BookListItem(
                    bookTitle = book.title,
                    bookAuthor = book.authors[0],
                    imageUrl = it.replace("http", "https"),
                    onClick = { navController.navigate(GoodbooksDestinations.DETAIL_ROUTE + "/${book.bookID}") }
                )
            }
        }
    }
    }
}
