package com.minhmdl.goodbooks.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grayseal.bookshelf.ui.theme.poppinsFamily
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import com.minhmdl.goodbooks.ui.theme.Black
import com.minhmdl.goodbooks.ui.theme.GreenIndicator
import com.minhmdl.goodbooks.utils.BookListItem
import com.minhmdl.goodbooks.utils.SearchInputField

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Search(navController = navController, searchViewModel = searchViewModel) { query ->
            searchViewModel.loading.value = true
            searchViewModel.searchBooks(query)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Results(searchViewModel = searchViewModel, navController = navController)
    }
}

@Composable
fun Search(
    navController: NavController,
    searchViewModel: SearchViewModel,
    onSearch: (String) -> Unit = {}
) {
    val searchState = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(searchState.value) {
        searchState.value.trim().isNotEmpty()
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = "Close Icon",
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .clickable(enabled = true, onClick = {
                    navController.navigate(GoodbooksDestinations.HOME_ROUTE)
                    searchViewModel.loading.value = false
                })
        )
        Text(
            "Search", fontSize = 16.sp,
            fontWeight = FontWeight.Medium, fontFamily = poppinsFamily, color = Black
        )

    }
    Spacer(modifier = Modifier.height(10.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
        SearchInputField(
            valueState = searchState,
            labelId = "Title, author or ISBN ",
            enabled = true,
            isSingleLine = false,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchState.value.trim())
                keyboardController?.hide()
            }
        )
    }
}

@Composable
fun Results(searchViewModel: SearchViewModel, navController: NavController) {

    val searchResults = searchViewModel.resultsState.value
    val listOfBooks = searchViewModel.listOfBooks.value

    if (listOfBooks.isNotEmpty()) {
        searchViewModel.loading.value = false
    }

    val loading = searchViewModel.loading.value

    if (loading) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(color = GreenIndicator)
        }
    }

    if (!(loading || searchResults.e != null || searchResults.data == null)) {
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Define constants for default values
            val DEFAULT_TITLE = "Title information unavailable"
            val DEFAULT_AUTHOR = "Author names not on record"
            val DEFAULT_IMAGE_URL = "https://media.istockphoto.com/id/1147544807/vector/thumbnail-image-vector-graphic.jpg?s=612x612&w=0&k=20&c=rnCKVbdxqkjlcs3xH87-9gocETqpspHFXu5dIGB4wuM="

            items(items = listOfBooks) { item ->
                var title = DEFAULT_TITLE
                var author = DEFAULT_AUTHOR
                var imageUrl = DEFAULT_IMAGE_URL
                if (item.imageLinks.toString().isNotBlank()) {
                    imageUrl = item.imageLinks.thumbnail.toString().trim().replace("http", "https")
                }
                if (item.title.isNotBlank()) {
                    title = item.title
                }
                if (!item.authors.firstOrNull().isNullOrBlank()) {
                    author = item.authors.joinToString(separator = ", ")
                }
                val bookId = item.bookID
                BookListItem(
                    bookTitle = title,
                    bookAuthor = author,
                    imageUrl = imageUrl,
                    onClick = {
                        navController.navigate(route = GoodbooksDestinations.DETAIL_ROUTE + "/$bookId")
                    }
                )
            }
        }
    }
}