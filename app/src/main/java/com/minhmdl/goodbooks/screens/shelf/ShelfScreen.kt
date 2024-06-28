package com.minhmdl.goodbooks.screens.shelf

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.grayseal.bookshelf.ui.theme.poppinsFamily
import com.minhmdl.goodbooks.R
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.model.Shelf
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import com.minhmdl.goodbooks.ui.theme.GreenIndicator
import com.minhmdl.goodbooks.utils.BookListItem
import com.minhmdl.goodbooks.utils.NavBar

@Composable
fun ShelfScreen(navController: NavController, shelfViewModel: ShelfViewModel) {
    val userId = Firebase.auth.currentUser?.uid
    var shelves: List<Shelf> by remember {
        mutableStateOf(mutableListOf())
    }
    val tabItemList = arrayOf(
        TabItem("Want to Read"),
        TabItem("Currently Reading"),
        TabItem("Read")
    )
    var loading by remember {
        mutableStateOf(true)
    }
    val context = LocalContext.current
    shelves = shelfViewModel.getShelves(userId, context, onDone = {
        loading = false
    })
    Scaffold(content = { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "My Books",
                    fontFamily = poppinsFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
            ShelfBooks(userId = userId, navController = navController, shelves = shelves, shelfViewModel = shelfViewModel)
            }
    },
        bottomBar = {
            NavBar(navController = navController)
        })
}
data class TabItem(
    val title :String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelfBooks(
    userId: String?,
    navController: NavController,
    shelves: List<Shelf>,
    shelfViewModel: ShelfViewModel
) {
    val shelfNames = shelves.map { it.name }
    val (selectedTab, setSelectedTab) = remember { mutableIntStateOf(0) }
    var booksInShelf by remember {
        mutableStateOf(mutableListOf<Book>())
    }
    val context = LocalContext.current

    var booksLoading by remember {
        mutableStateOf(true)
    }
    var favouritesLoading by remember {
        mutableStateOf(true)
    }
    var booksInFavourites: List<Book> by remember {
        mutableStateOf(emptyList())
    }
    var reviewsLoading by remember {
        mutableStateOf(true)
    }

    booksInFavourites = shelfViewModel.fetchFavourites(userId) {
        favouritesLoading = false
    }
    val selectedShelf = if (selectedTab >= 0 && selectedTab < shelves.size) {
        shelves[selectedTab]
    } else {
        Shelf("", emptyList())
    }
    booksInShelf = shelfViewModel.getBooksInAShelf(userId, context, selectedShelf.name) {
        booksLoading = false
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        PrimaryScrollableTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface,
            edgePadding = 5.dp

        ) {
            shelfNames.forEachIndexed { index, name ->
                Tab(
                    text = {
                        Text(
                            name,
                            fontFamily = poppinsFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            overflow = TextOverflow.Clip,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        )
                    },
                    selected = selectedTab == index,
                    onClick = {
                        setSelectedTab(index)
                        favouritesLoading = true
                        reviewsLoading = true
                        booksLoading = true
                    }
                )
            }
        }
        if (booksLoading && favouritesLoading && reviewsLoading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(color = GreenIndicator)
            }
        } else {
            if (booksInShelf.isNotEmpty()) {
                Spacer(modifier = Modifier.height(15.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 56.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(items = booksInShelf) { item ->
                        var title = "Title information unavailable"
                        var author = "Author names not on record"
                        var previewText = "Preview information not provided"
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
                        if (item.description.isNotEmpty()) {
                            val cleanDescription =
                                HtmlCompat.fromHtml(
                                    item.description,
                                    HtmlCompat.FROM_HTML_MODE_LEGACY
                                )
                            previewText = cleanDescription.toString()
                        }
                        val bookId = item.bookID

                        BookListItem(
                            bookTitle = title,
                            bookAuthor = author,
                            imageUrl = imageUrl,
                            onClick = {
                                navController.navigate(GoodbooksDestinations.DETAIL_ROUTE + "/$bookId")
                            }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_empty),
                        contentDescription = "Empty Shelf",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(bottom = 20.dp)
                    )
                    Text(
                        "You haven't added any books yet.",
                        fontFamily = poppinsFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                    Text(
                        "Explore books and add them to your shelves and tags.",
                        fontFamily = poppinsFamily,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = {navController.navigate(GoodbooksDestinations.HOME_ROUTE)}){
                        Text("Explore books")
                    }
                }
            }
        }
    }
}


