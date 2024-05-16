package com.minhmdl.goodbooks.screens.book

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.grayseal.bookshelf.ui.theme.loraFamily
import com.grayseal.bookshelf.ui.theme.poppinsFamily
import com.minhmdl.goodbooks.R
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.screens.shelf.ShelfViewModel
import com.minhmdl.goodbooks.ui.theme.Blue_Text
import com.minhmdl.goodbooks.ui.theme.Gray200
import com.minhmdl.goodbooks.ui.theme.Yellow
import com.minhmdl.goodbooks.utils.DataOrException
import com.minhmdl.goodbooks.utils.MenuSample
import com.minhmdl.goodbooks.utils.ShelvesAlertDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    shelfViewModel: ShelfViewModel,
    bookId: String?
) {

    var openDialog by remember {
        mutableStateOf(false)
    }

    val bookInfo = produceState(
        initialValue = DataOrException(loading = (true))
    ) {
        value = bookId?.let { bookViewModel.getBookInfo(it) }!!
    }.value

    val book = bookInfo.data

    val userId = Firebase.auth.currentUser?.uid
    val context = LocalContext.current

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)

    val scope = rememberCoroutineScope()
    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    var isFabVisible by remember { mutableStateOf(true) }
    var otherShelfName = ""
    var shelfName = ""

    androidx.compose.material.BottomSheetScaffold(
        scaffoldState = sheetScaffoldState,
        sheetElevation = 40.dp,
        sheetBackgroundColor = Color.White,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        // To dismiss bottomSheet when clicking outside on the screen
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    if (sheetState.isExpanded) {
                        sheetState.collapse()
                    }
                })
            },
//        floatingActionButton = {
//            if (isFabVisible) {
//                ExtendedFloatingActionButton(
//                    modifier = Modifier
//                        .padding(bottom = 50.dp),
//                    onClick = {
//                        isFabVisible = false
//                        scope.launch {
//                            if (sheetState.isCollapsed) {
//                                sheetState.expand()
//                            }
//                        }
//                    },
//
//                    contentColor = Color.White,
////                    containerColor = Color(0xff3d544e),
//                    content={
//                        Icon(
//                            Icons.Rounded.Add,
//                            contentDescription = "Add to My Books",
//                            tint = Color.White
//                        )
//                        Text(
//                            "Add to My Books",
//                            fontFamily = poppinsFamily,
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.White
//                        )
//                    }
//                )
//            }
//        },
        sheetContent = {
            BottomSheetContent(onSave = { name ->
                shelfName = name
                // Add book to shelf
                if (book != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val added = bookViewModel.addBookToShelf(
                            userId,
                            shelfName,
                            book,
                            context,
                            shelfExists = { shelfExistsName ->
                                otherShelfName = shelfExistsName
                                openDialog = true
                            })
                        if (added) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Successfully added book to $shelfName shelf",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else if (otherShelfName == "") {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Failed to add book to $shelfName shelf",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }, onRemove = {
                if (userId != null) {
                    bookViewModel.setBookToMove(book!!)
                    CoroutineScope(Dispatchers.IO).launch {
                        shelfViewModel.deleteABookInShelf(userId, book, shelfName)
                    }
                }
            })
        }
    ) {
        ShelvesAlertDialog(
            openDialog = openDialog,
            drawable = R.drawable.info,
            title = "Add to My Book",
            details = "The book is already on the $otherShelfName shelf. Would you like to move it to the $shelfName shelf instead?",
            onDismiss = { openDialog = false },
            onClick = {
                if (userId != null) {
                    bookViewModel.setBookToMove(book!!)
                    CoroutineScope(Dispatchers.IO).launch {
                        bookViewModel.moveBookToShelf(userId, shelfName, otherShelfName)
                    }
                    openDialog = false
                }
            }
        )
        Box {
            Details(navController, book)

            if (sheetState.isExpanded) {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.5f))
                        .fillMaxSize()
                ) {}
            }
        }
    }
    LaunchedEffect(sheetState.isCollapsed) {
        if (sheetState.isCollapsed) {
            isFabVisible = true
        }
    }
}

@Composable
fun Details(navController: NavController, book: Book?) {
    var bookTitle = "Unavailable"
    var bookAuthor = "Unavailable"
    var rating = "0"
    var genre = "Unavailable"
    var pages = "0"
    var isbn = "0"
    var description = "Preview information not provided"
    var imageUrl =
        "https://media.istockphoto.com/id/1147544807/vector/thumbnail-image-vector-graphic.jpg?s=612x612&w=0&k=20&c=rnCKVbdxqkjlcs3xH87-9gocETqpspHFXu5dIGB4wuM="

    // Check if data is available
    if (book != null) {
        if (book.title.isNotEmpty()) {
            bookTitle = book.title
        }
        if (book.authors[0].isNotEmpty()) {
            bookAuthor = book.authors.joinToString(separator = ", ")
        }
        if (book.ratingsCount.toString().isNotEmpty()) {
            rating = book.averageRating.toString()
        }
        if (book.industryIdentifiers.isNotEmpty()) {
            isbn = book.industryIdentifiers[0].identifier.toString()
        }
        if (book.categories[0].isNotEmpty()) {
            genre = book.categories[0]
            val words = genre.split("/")
                .map { it.trim() }
            val smallestWord =
                words.minByOrNull { it.length }
            genre = smallestWord ?: ""

        }
        if (book.pageCount.toString().isNotEmpty()) {
            pages = book.pageCount.toString()
        }
        if (book.description.isNotEmpty()) {
            val cleanDescription =
                HtmlCompat.fromHtml(book.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            description = cleanDescription.toString()
        }
        if (book.imageLinks.toString().isNotEmpty()) {
            imageUrl = book.imageLinks.thumbnail.toString().trim()
            imageUrl = imageUrl.replace("http", "https")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.details_background),
//            contentDescription = "backgroundImage",
//            modifier = Modifier.fillMaxWidth(),
//            contentScale = ContentScale.FillBounds
//        )
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            TopSection(navController = navController)
            if (book == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    androidx.compose.material3.LinearProgressIndicator(color = Color.White)
                }
            } else {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    BookImage(imageUrl = imageUrl)
                    BookDescription(
                        bookTitle = bookTitle,
                        bookAuthor = bookAuthor,
                        rating = rating,
                        genre = genre,
                        pages = pages,
                        isbn = isbn,
                        description = description
                    )
                    MenuSample()
                }
            }
        }
    }
}

@Composable
fun TopSection(navController: NavController) {
    Column(modifier = Modifier.padding(start = 15.dp, end = 20.dp, top = 15.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(80.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable(enabled = true, onClick = {
                        navController.popBackStack()
                    })
            )
        }
    }
}

@Composable
fun BookImage(imageUrl: String) {
    Column(modifier = Modifier.padding(25.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .error(R.drawable.cover_not_available)
                    .build(),
                contentDescription = "Book Image",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .scale(2.5f)
            )
        }
    }
}

@Composable
fun BookDescription(
    bookTitle: String,
    bookAuthor: String,
    rating: String,
    genre: String,
    isbn: String,
    pages: String,
    description: String
) {
    val time = (pages.toInt() * 1.7).toInt().toString()
    var restOfText by remember { mutableStateOf("") }
    val firstParagraph = description.substringBefore("\n\n")
    val remainingDescription = description.substringAfter(firstParagraph).substringAfter("\n\n")
    val isExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clip(
                shape = RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 15.dp
                )
            ),
        color = MaterialTheme.colorScheme.background

    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        bookTitle,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "$bookAuthor",
                        fontFamily = poppinsFamily,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until rating.toFloat().toInt()) {
                    Icon(Icons.Rounded.Star, contentDescription = "star", tint = Yellow)
                }
                if ((5 - rating.toFloat()) > 0) {
                    val unrated = 5 - rating.toFloat().toInt()
                    if ((rating.toFloat() - rating.toFloat().toInt()) > 0) {
                        Icon(
                            Icons.AutoMirrored.Rounded.StarHalf,
                            contentDescription = "star",
                            tint = Yellow
                        )
                        for (i in 0 until unrated - 1) {
                            Icon(
                                Icons.Rounded.Star,
                                contentDescription = "star",
                                tint = Color.LightGray
                            )
                        }
                    } else {
                        for (i in 0 until unrated) {
                            Icon(
                                Icons.Rounded.Star,
                                contentDescription = "star",
                                tint = Color.LightGray
                            )
                        }
                    }
                }
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground.copy(
                                    alpha = 0.4f
                                )
                            )
                        ) {
                            append(rating.toFloat().toString())
                        }
                        withStyle(
                            style = SpanStyle(
                                fontFamily = poppinsFamily,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(
                                    alpha = 0.2f
                                )
                            )
                        ) {
                            append(" / 5.0")
                        }
                    },
                    modifier = Modifier.padding(start = 5.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(vertical = 10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(10.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Genre",
                        fontFamily = poppinsFamily,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    )
                    Text(
                        genre,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Clip,
                        fontFamily = poppinsFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .padding(vertical = 5.dp)
                )
                Column(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Pages",
                        fontFamily = poppinsFamily,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    )
                    Text(
                        "$pages",
                        textAlign = TextAlign.Center,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .padding(vertical = 5.dp)
                )
                Column(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "ISBN",
                        fontFamily = poppinsFamily,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    )
                    Text(
                        "$isbn",
                        textAlign = TextAlign.Center,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                color = Gray200
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    Text(
                        text = stringResource(id = R.string.book_description),
                        fontFamily = poppinsFamily,
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Blue_Text,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
                Row {
                    Text(
                        text = firstParagraph.first().toString(),
                        fontFamily = loraFamily,
                        textAlign = TextAlign.Justify,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 60.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .align(Alignment.CenterVertically)
                    )

                    Text(
                        text = firstParagraph.drop(1),
                        textAlign = TextAlign.Justify,
                        fontFamily = poppinsFamily,
                        maxLines = 4,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        onTextLayout = { layoutResult ->
                            if (layoutResult.lineCount > 3) {
                                restOfText =
                                    firstParagraph.drop(1).substring(layoutResult.getLineEnd(3))
                            }
                        }
                    )
                }
                if (restOfText.isNotEmpty()) {
                    Text(
                        text = restOfText,
                        fontFamily = poppinsFamily,
                        textAlign = TextAlign.Justify,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                ExpandingText(
                    modifier = Modifier.padding(top = 10.dp),
                    text = remainingDescription
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun MenuContent(bookViewModel: BookViewModel,
                userId: String?,
                context: Context,
                shelfExits: (String) -> Unit,
                book: Book?){
    var expanded by remember { mutableStateOf(false) }
    val menuItems = listOf(
        "Want to Read",
        "Currently Reading",
        "Read",
    )
    var openDialog by remember { mutableStateOf(false) }
    var selectedItemIndex by remember { mutableStateOf(0) }
    var otherShelfName by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {

        OutlinedCard(modifier = Modifier.padding(15.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(125.dp)
                    .height(50.dp)
                    .padding(3.dp)
                    .clickable {
                        expanded = true
                    }
            ) {
                Text(
                    text = menuItems[selectedItemIndex],
                    fontFamily = poppinsFamily,
                    fontSize = 13.sp

                )
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                    colorFilter = ColorFilter.tint(Color.Black),
                    contentDescription = "Arrow drop down"

                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.requiredSizeIn(maxHeight = 300.dp)
            ) {
                menuItems.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            expanded = false
                            selectedItemIndex = index
                            if (book!= null){
                                CoroutineScope(Dispatchers.IO).launch {
                                    val added = bookViewModel.addBookToShelf(
                                        userId,
                                        item,
                                        book,
                                        context,
                                        shelfExists = { ShelfExistsName ->
                                                otherShelfName = ShelfExistsName
                                                openDialog = true
                                        }

                                    )
                                }
                            }
                        },
                        trailingIcon = {
                            if (selectedItemIndex == index) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Selected Icon"
                                )
                            }
                        }
                    )
                }
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text(text = "Remove from My Books") },
                    onClick = {
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ExpandingText(modifier: Modifier = Modifier, text: String) {
    var isExpanded by remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    var isClickable by remember { mutableStateOf(false) }
    var finalText by remember { mutableStateOf(text) }

    val textLayoutResult = textLayoutResultState.value
    LaunchedEffect(textLayoutResult) {
        if (textLayoutResult == null) return@LaunchedEffect

        when {
            isExpanded -> {
                finalText = "$text Show Less"
            }

            !isExpanded && textLayoutResult.hasVisualOverflow -> {
                val lastCharIndex = textLayoutResult.getLineEnd(2)
                val showMoreString = "... Show More"
                val adjustedText = text
                    .substring(startIndex = 0, endIndex = lastCharIndex)
                    .dropLast(showMoreString.length)
                    .dropLastWhile { it == ' ' || it == '.' }

                finalText = "$adjustedText$showMoreString"

                isClickable = true
            }
        }
    }

    Text(
        text = finalText,
        fontFamily = poppinsFamily,
        textAlign = TextAlign.Justify,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onBackground,
        maxLines = if (isExpanded) Int.MAX_VALUE else 3,
        onTextLayout = { textLayoutResultState.value = it },
        modifier = modifier
            .clickable(enabled = isClickable) { isExpanded = !isExpanded }
            .animateContentSize(),
    )
}

@Composable
fun BottomSheetContent(onSave: (String) -> Unit, onRemove: () -> Unit) {
    var selectedOption by remember { mutableStateOf("") }
    val options = listOf("Currently Reading", "Want to Read", "Read")

    Column(
        modifier = Modifier
            .height(350.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            HorizontalDivider(
                Modifier
                    .width(40.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(15.dp))
            )
        }
        Text(
            text = "Add to My Books",
            fontFamily = poppinsFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 15.dp)
        )
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        selectedOption = option
                        onSave(option)
                    }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    option,
                    fontFamily = poppinsFamily,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                RadioButton(
                    selected = selectedOption == option,
                    onClick = {
                        selectedOption = option
                        onSave(option)
                    }
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onRemove),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Remove from My Books",
                fontFamily = poppinsFamily,
                fontSize = 14.sp,
                color = Color.Red,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}
