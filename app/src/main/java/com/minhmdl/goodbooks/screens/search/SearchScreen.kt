package com.minhmdl.goodbooks.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.grayseal.bookshelf.ui.theme.poppinsFamily
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import com.minhmdl.goodbooks.ui.theme.Black
import com.minhmdl.goodbooks.ui.theme.GreenIndicator
import com.minhmdl.goodbooks.utils.HistoryCard
import com.minhmdl.goodbooks.utils.SearchCard
import com.minhmdl.goodbooks.utils.SearchInputField
import com.minhmdl.goodbooks.utils.convertToMutableList


/**
A composable function that displays the Search Screen where users can search for books, display their recent search history
 * and see the results of their search.
 */
@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel
) {
    val userId = Firebase.auth.currentUser?.uid

    val previousSearches: MutableList<String> by remember {
        mutableStateOf(mutableListOf())
    }

    var displayPreviousHistory by remember {
        mutableStateOf(false)
    }

    if (userId != null) {
        val db = FirebaseFirestore.getInstance().collection("users").document(userId).get()
        db.addOnSuccessListener {
            val data = db.result.get("searchHistory")
            if (data != null) {
                for (i in data as MutableList<*>) {
                    previousSearches.add(i as String)
                }
                displayPreviousHistory = previousSearches.isNotEmpty()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Search(navController = navController, searchViewModel = searchViewModel) { query ->
            searchViewModel.loading.value = true
            searchViewModel.searchBooks(query)

            if (userId != null) {
                val db = FirebaseFirestore.getInstance().collection("users").document(userId).get()
                db.addOnSuccessListener {
                    val data = db.result.get("searchHistory")
                    val response = convertToMutableList(data)
                    response?.add(query)
                    FirebaseFirestore.getInstance().collection("users").document(userId)
                        .update("searchHistory", response)
                }
            }
        }
        if (displayPreviousHistory) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Recent Searches",
                    fontSize = 13.sp,
                    fontFamily = poppinsFamily,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val lastThree = if (previousSearches.size >= 4) {
                        previousSearches.subList(previousSearches.size - 3, previousSearches.size)
                    } else {
                        previousSearches
                    }
                    val items = lastThree.toSet().toList().asReversed()
                    items.forEach {
                        if (it != "") {
                            HistoryCard(text = it, onClick = {
                                searchViewModel.loading.value = true
                                searchViewModel.searchBooks(it)
                            })
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Results(searchViewModel = searchViewModel, navController = navController)
    }
}

/**
Composable function that displays the search UI.
 */

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
    Spacer(modifier = Modifier.height(30.dp))
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
                searchState.value = ""
            }
        )
    }
}

/**
A composable function that displays the search results obtained by querying the Google Books API.
 */
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
            LinearProgressIndicator(color=GreenIndicator)
        }
    }

    if (!(loading || searchResults.e != null || searchResults.data == null)) {
        Spacer(modifier = Modifier.height(15.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(items = listOfBooks) { item ->
                var title = "Title information unavailable"
                var author = "Author names not on record"
                var imageUrl =
                    "https://media.istockphoto.com/id/1147544807/vector/thumbnail-image-vector-graphic.jpg?s=612x612&w=0&k=20&c=rnCKVbdxqkjlcs3xH87-9gocETqpspHFXu5dIGB4wuM="
                if (item.imageLinks.toString().isNotEmpty()) {
                    imageUrl = item.imageLinks.thumbnail.toString().trim()
                    imageUrl = imageUrl.replace("http", "https")
                }
                if (item.title.isNotEmpty()) {
                    title = item.title
                }
                if (item.authors[0].isNotEmpty()) {
                    author = item.authors.joinToString(separator = ", ")
                }
                val bookId = item.bookID
                SearchCard(
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