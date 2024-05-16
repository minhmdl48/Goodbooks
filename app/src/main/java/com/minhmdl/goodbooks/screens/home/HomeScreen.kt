package com.minhmdl.goodbooks.screens.home

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.grayseal.bookshelf.ui.theme.loraFamily
import com.grayseal.bookshelf.ui.theme.poppinsFamily
import com.minhmdl.goodbooks.R
import com.minhmdl.goodbooks.data.StoreUserName
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import com.minhmdl.goodbooks.screens.category.categories
import com.minhmdl.goodbooks.screens.login.LoginScreen
import com.minhmdl.goodbooks.screens.login.LoginViewModel
import com.minhmdl.goodbooks.screens.search.SearchViewModel
import com.minhmdl.goodbooks.ui.theme.Black
import com.minhmdl.goodbooks.ui.theme.Gray200
import com.minhmdl.goodbooks.ui.theme.GreenIndicator
import com.minhmdl.goodbooks.ui.theme.White
import com.minhmdl.goodbooks.utils.Category
import com.minhmdl.goodbooks.utils.GoodbooksNavigationDrawerItem
import com.minhmdl.goodbooks.utils.NavBar
import com.minhmdl.goodbooks.utils.Reading
import com.minhmdl.goodbooks.utils.ShelvesAlertDialog
import kotlinx.coroutines.launch
import java.util.*

/**
 * Composable function that displays the Home Screen of the app.
 */
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
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.account)
    val avatar: Bitmap = bitmap
    val scope = rememberCoroutineScope()
    val nameDataStore = StoreUserName(context)

    if (user == null) {
        LoginScreen(navController, loginViewModel, nameDataStore)
    } else {
        val name = nameDataStore.getName.collectAsState(initial = "")
        HomeContent(
            user = user!!,
            name = name.value,
            avatar = avatar,
            navController = navController,
            searchViewModel = searchViewModel,
            reading = readingList.reversed(),
            loading = loading
        )
    }
}

@Composable
fun HomeContent(
    user: FirebaseUser,
    name: String?,
    avatar: Bitmap,
    navController: NavController,
    searchViewModel: SearchViewModel,
    reading: List<Book>,
    loading: Boolean
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var currentRead: Book by remember {
        mutableStateOf(Book())
    }
    currentRead = try {
        reading.first()
    } catch (e: Exception) {
        Book()
    }

    val readingBooksTotal = reading.size
    val scope = rememberCoroutineScope()
    val items = mapOf(
        "Log Out" to R.drawable.logout
//        "Delete Account" to R.drawable.delete
    )
    val selectedItem = remember { mutableStateOf(items["Log Out"]) }
    var openDialog by remember {
        mutableStateOf(false)
    }

    val bitmap = remember {
        mutableStateOf(avatar)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(250.dp),
                drawerShape = RectangleShape,
                drawerContainerColor = MaterialTheme.colorScheme.background,
                drawerTonalElevation = 0.dp,
            ) {
                Spacer(Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 20.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ConstraintLayout {
                        val (profile, edit) = createRefs()
                        Surface(
                            modifier = Modifier
                                .size(60.dp)
                                .background(color = Color.Transparent, shape = CircleShape)
                                .constrainAs(profile) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                }
                                .clickable(onClick = {}),
                            shape = CircleShape,
                        ) {
                            val image = bitmap.value
                            Image(
                                bitmap = image.asImageBitmap(),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable(onClick = {}),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Hi, ${name?.substringBefore(" ")}!", fontFamily = loraFamily,
                        fontSize = 23.sp,
                        overflow = TextOverflow.Clip,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(Modifier.height(20.dp))
                    Text(
                        "$readingBooksTotal books in your reading list", fontFamily = poppinsFamily,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(20.dp))
                    items.forEach { item ->
                        val selectedValue = selectedItem.value
                        val selectedKey = items.entries.find { it.value == selectedValue }?.key
                        ShelvesAlertDialog(
                            openDialog = openDialog,
                            drawable = R.mipmap.ic_book,
                            size = 50.dp,
                            color = Color.Transparent,
                            title = if (selectedKey == "Log Out") {
                                "Confirm Logout"
                            } else {
                                "Delete Account"
                            },

                            details = if (selectedKey == "Log Out") {
                                "Do you want to Logout ?"
                            } else {
                                "You are about to delete your account. Continues?"
                            },
                            onDismiss = {
                                openDialog = false
                            },
                            onClick = {
                                if (selectedKey == "Log Out") Firebase.auth.signOut()
                                else {
                                    val db = FirebaseFirestore.getInstance()
                                    val userId = Firebase.auth.currentUser?.uid

                                    userId?.let {
                                        db.collection("users").document(it)
                                            .delete()
                                            .addOnSuccessListener {
                                                Log.d(
                                                    "HomeScreen",
                                                    "User data successfully deleted!"
                                                )
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w("HomeScreen", "Error deleting user data", e)
                                            }
                                    }
                                    user.delete()
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.d(TAG, "User account deleted.")
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "deleteUser:failure", task.exception)

                                            }
                                        }

                                }
                                navController.navigate(GoodbooksDestinations.LOGIN_ROUTE)
                            }
                        )
                        GoodbooksNavigationDrawerItem(
                            icon = {
                                androidx.compose.material3.Icon(
                                    painter = painterResource(id = item.value),
                                    contentDescription = item.key,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .background(color = Color.Transparent),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            },
                            label = {
                                Text(
                                    item.key,
                                    fontFamily = poppinsFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 15.sp
                                )
                            },
                            selected = item.value == selectedItem.value,
                            onClick = {
                                selectedItem.value = item.value
//                                if (item.key == "Log Out" || item.key == "Delete Account") {
                                if (item.key == "Log Out") {
                                    openDialog = true
                                }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = MaterialTheme.colorScheme.background,
                                unselectedContainerColor = Color.Transparent,
                                selectedIconColor = Black,
                                unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                                selectedTextColor = Black,
                                unselectedTextColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }

                }
            }
        },
        scrimColor = Color.Transparent,
        content = {
            Scaffold(content = { padding ->
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    HomeTopHeader(navController, searchViewModel, avatar = avatar) {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        color = Gray200
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
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
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        color = Gray200
                    )

                    Categories(navController = navController)

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        color = Gray200
                    )
                    ReadingList(
                        navController,
                        loading = loading,
                        readingList = reading
                    )
                }
            },
                bottomBar = {
                    NavBar(navController)
                })
        }
    )
}

/**
 * HomeTopHeader: contains the user's profile picture and a search icon that navigates to the search screen.
 */
@Composable
fun HomeTopHeader(
    navController: NavController,
    searchViewModel: SearchViewModel,
    avatar: Bitmap,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 18.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .size(48.dp)
                .background(color = Color.Transparent, shape = CircleShape),
            shape = CircleShape,
        ) {
            Image(
                bitmap = avatar.asImageBitmap(),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        enabled = true,
                        onClick = {
                            onProfileClick()
                        },
                    ),
                contentScale = ContentScale.Crop
            )
        }
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .clickable(enabled = true, onClick = {
                        searchViewModel.loading.value = false
                        searchViewModel.listOfBooks.value = listOf()
                        navController.navigate(GoodbooksDestinations.SEARCH_ROUTE)
                    }),
                shape = CircleShape,
                color = Color.Transparent,
                border = BorderStroke(
                    width = 0.9.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                    modifier = Modifier
                        .size(20.dp)
                        .padding(10.dp)
                        .clip(CircleShape)
                        .background(color = Color.Transparent, shape = CircleShape),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }
        }
    }
}

/**
 * A composable function that displays a list of categories using a LazyRow layout. Each category is
 * represented by a Category composable that is clickable and navigates to the corresponding screen.
 * @param navController The NavController used for navigating between screens.
 */
@Composable
fun Categories(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 5.dp),
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

        Spacer(modifier = Modifier.height(3.dp))

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
            LazyColumn(
                modifier = Modifier
                    .padding(top = 5.dp, start = 20.dp, end = 20.dp, bottom = 56.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                itemsIndexed(items = readingList) { index: Int, item: Book ->
                    var genre = item.categories[0]
                    val words = genre.split("/")
                        .map { it.trim() }
                    val smallestWord =
                        words.minByOrNull { it.length }
                    genre = smallestWord ?: ""
                    if (genre == "") {
                        genre = "Unavailable"
                    }
                    item.imageLinks.thumbnail?.let {
                        Reading(
                            genre = genre,
                            bookAuthor = item.authors[0],
                            bookTitle = item.title,
                            imageUrl = it.replace("http", "https"),
                            rating = item.averageRating.toString(),
                            onClick = { navController.navigate(GoodbooksDestinations.DETAIL_ROUTE + "/${item.bookID}") }
                        )
                    }
                }
            }
        }
    }
}
