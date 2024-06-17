package com.minhmdl.goodbooks.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.grayseal.bookshelf.ui.theme.poppinsFamily
import com.minhmdl.goodbooks.data.StoreUserName
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import com.minhmdl.goodbooks.screens.category.categories
import com.minhmdl.goodbooks.screens.login.LoginScreen
import com.minhmdl.goodbooks.screens.login.LoginViewModel
import com.minhmdl.goodbooks.screens.search.SearchIt
import com.minhmdl.goodbooks.screens.search.SearchViewModel
import com.minhmdl.goodbooks.ui.theme.Black
import com.minhmdl.goodbooks.ui.theme.GreenIndicator
import com.minhmdl.goodbooks.utils.BookListItem
import com.minhmdl.goodbooks.utils.Category
import com.minhmdl.goodbooks.utils.NavBar
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    searchViewModel: SearchViewModel,
    homeViewModel: HomeViewModel
) {
    val user by remember { mutableStateOf(Firebase.auth.currentUser) }
    var readingList by remember {
        mutableStateOf(mutableListOf<Book>())
    }

    val context = LocalContext.current
    val userId = user?.uid

    var loading by remember {
        mutableStateOf(true)
    }

    readingList = homeViewModel.getBooksInReadingList(
        userId = userId,
        context = context,
        onDone = { loading = false }
    )

    val nameDataStore = StoreUserName(context)

    if (user == null) {
        LoginScreen(navController, loginViewModel, nameDataStore)
    } else {
        val name = nameDataStore.getName.collectAsState(initial = "")
        HomeContent(
            name = name.value,
            navController = navController,
            searchViewModel = searchViewModel,
            reading = readingList.reversed(),
            loading = loading
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    name: String?,
    navController: NavController,
    searchViewModel: SearchViewModel,
    reading: List<Book>,
    loading: Boolean
) {
    var currentRead: Book by remember {
        mutableStateOf(Book())
    }
    currentRead = try {
        reading.first()
    } catch (e: Exception) {
        Book()
    }

    Scaffold(content = { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(top = 55.dp,),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    "Welcome back, ${name?.substringBefore(" ")}!",
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = Black,
                    modifier = Modifier.padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
                )
            }
            item {
                Text(
                    "What do you want to read today?",
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = Black,
                    modifier = Modifier.padding(
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
                )
            }

           item {
               Categories(navController = navController)
           }

            item {
                ReadingList(
                    navController,
                    loading = loading,
                    readingList = reading
                )
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
fun Categories(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            "Categories",
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Black,
            modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        val keysList = categories.keys.toList()
        LazyRow(
            modifier = Modifier.padding(bottom = 10.dp, end = 0.dp, start = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            itemsIndexed(items = keysList) { index: Int, item: String ->
                if (index == 0) {
                    Spacer(modifier = Modifier.width(20.dp))
                    categories[item]?.let {
                        Category(
                            category = item,
                            image = it,
                            onClick = { navController.navigate(route = GoodbooksDestinations.CATEGORY_ROUTE + "/$item") })
                    }
                } else {
                    Category(
                        category = item,
                        image = categories[item]!!,
                        onClick = { navController.navigate(route = GoodbooksDestinations.CATEGORY_ROUTE + "/$item") })
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoriesPreview() {
    Categories(navController = rememberNavController())
}

/**
 * Composable function that displays the user's reading list, with the book cover images,
 * the title and author of the book, and an onClick listener that navigates to a book detail screen.
 * @param onClick The function that handles clicks on the reading list items.
 * */

@Composable
fun ReadingList(navController: NavController, loading: Boolean, readingList: List<Book>) {
    Text(
        "My Reading List",
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(start = 20.dp, end = 20.dp)
    )
    if (loading) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            androidx.compose.material.LinearProgressIndicator(color = GreenIndicator)
        }
    } else {
        if (readingList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(bottom = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Uh oh, you have no current reads!",
                    fontFamily = poppinsFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(56.dp))
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(top = 5.dp, start = 20.dp, end = 20.dp, bottom = 56.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                for (item in readingList) {
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
    }
}
