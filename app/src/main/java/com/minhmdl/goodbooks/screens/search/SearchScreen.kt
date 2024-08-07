package com.minhmdl.goodbooks.screens.search

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grayseal.bookshelf.ui.theme.poppinsFamily
import com.minhmdl.goodbooks.R
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import com.minhmdl.goodbooks.screens.category.categoriesList
import com.minhmdl.goodbooks.ui.theme.GreenIndicator
import com.minhmdl.goodbooks.utils.BookListItem
import com.minhmdl.goodbooks.utils.GoodbooksDivider
import com.minhmdl.goodbooks.utils.NavBar

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel
) {
    Scaffold(content = { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search1),
                        contentDescription = "Search",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(20.dp)
                    )
                }
            }
            item {
                Text(
                    "See new releases, most-read books, quotes, lists, and more in these popular genres.",
                    fontFamily = poppinsFamily,
                    fontSize = 16.sp,
                    maxLines=2,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
            }
            items(categoriesList.keys.toList()) { category ->
                Log.d("Search","Category: $category")
                Row(
                    modifier = Modifier
                        .padding(top=5.dp, start=20.dp)
                        .clickable {
                            navController.navigate("category/$category")
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Arrow Icon"
                    )
                }
                GoodbooksDivider()
            }
            item{
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    },
        bottomBar = {
            NavBar(navController)
        },
        topBar = {
            SearchIt(searchViewModel = searchViewModel, navController = navController)
        }
    )
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
            val DEFAULT_IMAGE_URL =
                "https://media.istockphoto.com/id/1147544807/vector/thumbnail-image-vector-graphic.jpg?s=612x612&w=0&k=20&c=rnCKVbdxqkjlcs3xH87-9gocETqpspHFXu5dIGB4wuM="

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchIt(
    searchViewModel: SearchViewModel,
    navController: NavController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    SearchBar(
        query = searchQuery,
        onQueryChange = { searchQuery = it },
        onSearch = {
            searchViewModel.searchBooks(searchQuery)
            keyboardController?.hide()
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = {
            Text(
                text = "Title, author or ISBN",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
        },
        leadingIcon = {
            if(!active) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            }else{
                IconButton(onClick={
                    active=false
                    searchQuery=""
                    searchViewModel.listOfBooks.value = listOf()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { searchQuery = "" }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Clear search"
                    )
                }
            }
        },
        content = {
            LazyColumn {
                itemsIndexed(items = searchViewModel.listOfBooks.value) { index: Int, item: Book ->
                    item.imageLinks.thumbnail?.let {
                        BookListItem(
                            bookTitle = item.title,
                            bookAuthor = item.authors[0],
                            imageUrl = it.replace("http", "https"),
                            onClick = { navController.navigate(GoodbooksDestinations.DETAIL_ROUTE + "/${item.bookID}") }
                        )
                    }
                }
            }
        }
    )
}
