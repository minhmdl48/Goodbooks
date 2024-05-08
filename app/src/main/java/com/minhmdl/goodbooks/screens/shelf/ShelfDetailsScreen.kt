//package com.minhmdl.goodbooks.screens.shelf
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Scaffold
//import androidx.compose.material.Text
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.google.firebase.Firebase
//import com.google.firebase.auth.auth
//import com.grayseal.bookshelf.ui.theme.poppinsFamily
//import com.minhmdl.goodbooks.model.Book
//import java.lang.reflect.Modifier
//import androidx.compose.foundation.layout.Row as Row
//
//@Composable
//fun ShelfDetailsScreen(shelfName: String, navController: NavController, shelfViewModel: ShelfViewModel) {
//    val userId = Firebase.auth.currentUser?.uid
//    var booksInShelf by remember {
//        mutableStateOf(mutableListOf<Book>())
//    }
//    val context = LocalContext.current
//    var loading by remember {
//        mutableStateOf(true)
//    }
//    booksInShelf = shelfViewModel.getBooksInAShelf(userId, context, shelfName, onDone = {
//        loading = false
//    })
//    Scaffold(content = { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(20.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    shelfName,
//                    fontFamily = poppinsFamily,
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.onBackground,
//                    textAlign = TextAlign.Center
//                )
//            }
//            if (!loading) {
//                if (booksInShelf.isNotEmpty()) {
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 15.dp),
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        items(items = booksInShelf) { book ->
//                            // Display a BookCard for each book
//                            BookCard(
//                                shelfViewModel,
//                                userId = userId,
//                                book = book,
//                                shelfName = shelfName,
//                                favourite = false, // Replace with actual value
//                                reviewed = false, // Replace with actual value
//                                bookTitle = book.title,
//                                bookAuthor = book.authors.joinToString(),
//                                previewText = book.description,
//                                imageUrl = book.imageLinks.thumbnail,
//                                onShelfChanged = { /* Handle shelf change */ },
//                                onClick = { /* Handle click */ }
//                            )
//                        }
//                    }
//                } else {
//                    Text("No books in this shelf")
//                }
//            } else {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    LinearProgressIndicator(color = GreenIndicator)
//                }
//            }
//        }
//    })
//}