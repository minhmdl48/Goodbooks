package com.minhmdl.goodbooks.utils

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mode
import androidx.compose.material.icons.outlined.InsertEmoticon
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.grayseal.bookshelf.ui.theme.poppinsFamily
import com.minhmdl.goodbooks.R
import com.minhmdl.goodbooks.model.Book
import com.minhmdl.goodbooks.navigation.BottomNavItem
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import com.minhmdl.goodbooks.screens.AddBooksScreen
import com.minhmdl.goodbooks.screens.book.BookViewModel
import com.minhmdl.goodbooks.ui.theme.Black
import com.minhmdl.goodbooks.ui.theme.Gray200
import com.minhmdl.goodbooks.ui.theme.Green
import com.minhmdl.goodbooks.ui.theme.iconColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    EmailInputField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun NameInput(
    nameState: MutableState<String>,
    labelId: String = "Name",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    var icon by remember {
        mutableStateOf(Icons.Outlined.SentimentSatisfied)
    }
    OutlinedTextField(
        value = nameState.value,
        onValueChange = {
            nameState.value = it
            icon = Icons.Outlined.InsertEmoticon
        },
        placeholder = { Text(text = labelId, fontFamily = poppinsFamily, fontSize = 15.sp) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "Name Icon"
            )
        },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 15.sp,
            fontFamily = poppinsFamily,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        ),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = Black,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            focusedLeadingIconColor = Color.Black,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
            focusedPlaceholderColor = Black,
            selectionColors = TextSelectionColors(
                handleColor = Color.Black,
                backgroundColor = Green
            )
        )
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done
) {
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    var error by remember {
        mutableStateOf(false)
    }
    if (error) {
        Text(
            "* Password must be at least 6 characters", fontSize = 14.sp,
            fontFamily = poppinsFamily,
            color = Color.Red,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {
            passwordState.value = it
            error = passwordState.value.length < 6
        },
        placeholder = { Text(text = labelId, fontFamily = poppinsFamily, fontSize = 14.sp) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = "Lock Icon"
            )
        },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontFamily = poppinsFamily,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = {
            PasswordVisibility(passwordVisibility = passwordVisibility)
        },
        keyboardActions = KeyboardActions {
            keyboardController?.hide()
        },

        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = Black,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            focusedLeadingIconColor = Color.Black,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
            focusedPlaceholderColor = Black,
            selectionColors = TextSelectionColors(
                handleColor = Color.Black,
                backgroundColor = Green
            )
        )
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        if (visible) {
            Icon(
                imageVector = Icons.Outlined.Visibility,
                contentDescription = "Visibility Icon",
                tint = iconColor
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.VisibilityOff,
                contentDescription = "Visibility Icon",
                tint = iconColor
            )
        }
    }
}

@Composable
fun EmailInputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    var error by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    if (error) {
        Text(
            "* Invalid email address", fontSize = 13.sp,
            fontFamily = poppinsFamily,
            color = Color.Red,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {
            valueState.value = it
            error = !isValidEmail(valueState.value)
        },
        placeholder = { Text(text = labelId, fontFamily = poppinsFamily, fontSize = 14.sp) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.AlternateEmail,
                contentDescription = "Email Icon"
            )
        },
        singleLine = isSingleLine,
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontFamily = poppinsFamily,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .fillMaxWidth(),
        enabled = enabled,
        keyboardActions = KeyboardActions {
            keyboardController?.hide()
        },
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = Black,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            focusedLeadingIconColor = Color.Black,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
            focusedPlaceholderColor = Black,
            selectionColors = TextSelectionColors(
                handleColor = Color.Black,
                backgroundColor = Green
            )
        )
    )
}

@Composable
fun SubmitButton(textId: String, loading: Boolean, validInputs: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        enabled = !loading && validInputs, shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Green,
            disabledContainerColor = Green
        ),
        elevation = ButtonDefaults.buttonElevation(10.dp)
    ) {
        if (loading) CircularProgressIndicator(color = Color.Blue) else
            if (validInputs) {
                Text(
                    text = textId,
                    fontFamily = poppinsFamily,
                    modifier = Modifier.padding(5.dp),
                    fontSize = 15.sp,
                    color = Black,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Text(
                    text = textId,
                    fontFamily = poppinsFamily,
                    modifier = Modifier.padding(5.dp),
                    fontSize = 15.sp,
                    color = Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
    }
}

@Composable
fun Category(category: String, image: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(50.dp)
                .background(color = MaterialTheme.colorScheme.onPrimary, shape = CircleShape)
                .clickable(onClick = onClick),
            color = MaterialTheme.colorScheme.onPrimary
        ) {
            Image(
                painter = painterResource(id = image),
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ),
                contentDescription = "Category"
            )
        }
        Text(
            category,
            fontFamily = poppinsFamily,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}

@Preview
@Composable
fun CategoryPreview() {
    Category("Fiction", R.drawable.finance) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(
            name = stringResource(id = R.string.home),
            route = GoodbooksDestinations.HOME_ROUTE,
            selectedIcon = R.drawable.ic_home_selected,
            unselectedIcon = R.drawable.ic_home_unselected
        ),
        BottomNavItem(
            name = stringResource(id = R.string.home_mybooks),
            route = GoodbooksDestinations.SHELF_ROUTE,
            selectedIcon = R.drawable.ic_shelf_selected,
            unselectedIcon = R.drawable.ic_shelf_unselected
        ),
        BottomNavItem(
            name = stringResource(id = R.string.home_search),
            route = GoodbooksDestinations.SEARCH_ROUTE,
            selectedIcon = R.drawable.ic_search,
            unselectedIcon = R.drawable.ic_search
        ),
        BottomNavItem(
            name = stringResource(id = R.string.stats),
            route = GoodbooksDestinations.STATS_ROUTE,
            selectedIcon = R.drawable.ic_pie_chart_selected,
            unselectedIcon = R.drawable.ic_pie_chart
        ),
        BottomNavItem(
            name = stringResource(id = R.string.profile),
            route = GoodbooksDestinations.PROFILE_ROUTE,
            selectedIcon = R.drawable.ic_profile_selected,
            unselectedIcon = R.drawable.ic_profile_unselected
        )
    )

    val backStackEntry = navController.currentBackStackEntryAsState()

    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            items.forEach { item ->
                val selected = item.route == backStackEntry.value?.destination?.route
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = if (selected) {
                                painterResource(id = item.selectedIcon)
                            } else {
                                painterResource(id = item.unselectedIcon)
                            },
                            contentDescription = item.name,
                            modifier = Modifier
                                .size(30.dp)
                                .background(color = Color.Transparent)
                        )
                    },
                    label = {},
                    selected = selected,
                    onClick = {
                        when (item.name) {
                            "Home" -> navController.navigate(GoodbooksDestinations.HOME_ROUTE)
                            "My Books" -> navController.navigate(GoodbooksDestinations.SHELF_ROUTE)
                            "Statistics" -> navController.navigate(GoodbooksDestinations.STATS_ROUTE)
                            "Search" -> navController.navigate(GoodbooksDestinations.SEARCH_ROUTE)
                            "Profile" -> navController.navigate(GoodbooksDestinations.PROFILE_ROUTE)
                        }
                    },
                    colors = NavigationBarItemColors(
                        selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        disabledIconColor = MaterialTheme.colorScheme.onSurface,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        selectedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),

                    interactionSource = MutableInteractionSource()
                )
            }
        }
    }
}

@Preview
@Composable
fun NavBarPreview() {
    NavBar(navController = rememberNavController())
}

@Composable
fun BookListItem(
    bookTitle: String,
    bookAuthor: String,
    imageUrl: String,
    onClick: () -> Unit
) {
    Spacer(modifier = Modifier.height(5.dp))
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(5.dp),
        color = Color(0xF7F7F7)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .error(R.drawable.cover_not_available)
                    .build(),
                contentDescription = "Book Image",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.size(130.dp),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 5.dp)
                    .offset(y = (-10).dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    bookTitle,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    maxLines = 2,
                    minLines = 1,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    bookAuthor,
                    overflow = TextOverflow.Clip,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                )
            }
        }
    }
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        color = Gray200
    )
}

@Composable
fun MenuSample(
    book: Book,
    bookViewModel: BookViewModel,
    context: Context
) {
    val userId = Firebase.auth.currentUser?.uid
    var addbooksVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var reviewText by remember { mutableStateOf("") }
    var isDone by remember { mutableStateOf(false) }
    var isReading by remember { mutableStateOf(false) }
    var pagesInProgress by remember { mutableStateOf(0) }
    LaunchedEffect(book.bookID) {
        pagesInProgress = bookViewModel.getProgressReading(userId, book.bookID)
        if (bookViewModel.getShelfName(userId, book) == "Currently Reading") {
            isReading = true
        }
        if (bookViewModel.getShelfName(userId, book) == "Read") {
            isDone = true
        }
        reviewText = bookViewModel.getReview(userId, book.bookID)
    }
    var openProgressDialog by remember { mutableStateOf(false) }
    var openReviewDialog by remember { mutableStateOf(false) }
    var selectedItemIndex by remember { mutableStateOf(0) }
    var itemSelected by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { addbooksVisible = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Add")
        }

        AnimatedVisibility(
            visible = addbooksVisible,
            enter = slideInVertically() + expandVertically(
                expandFrom = Alignment.Top
            ) + fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            AddBooksScreen(
                onOptionSelected = { selected, answer ->
                    itemSelected = selected
                    selectedItemIndex = answer
                },
                onDismiss = { addbooksVisible = false },
                book = book,
                bookViewModel = bookViewModel,
                context = context
            )
        }
        if (isDone) {
            if (reviewText != "") {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = "Your review",
                            modifier = Modifier.padding(start = 2.dp),
                            fontSize = 17.sp,
                            textDecoration = TextDecoration.Underline
                        )
                        IconButton(onClick = { openReviewDialog = true }) {
                            Icon(imageVector = Icons.Filled.Mode, contentDescription = null)
                        }
                    }

                    TextField(value = reviewText, onValueChange = {}, readOnly = true)
                }
            } else {
                Button(
                    onClick = { openReviewDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Write a Review")
                }
            }
            AnimatedVisibility(
                visible = openReviewDialog,
                enter = slideInVertically() + expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn(initialAlpha = 0.3f),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                ReviewDialog(
                    book,
                    reviewText,
                    onDismiss = { openReviewDialog = false },
                    bookViewModel,
                    userId
                )
            }
            DatePickerWithDialog(modifier = Modifier.padding(5.dp),bookViewModel, book)

        }
        if (isReading) {
            OutlinedButton(
                onClick = { openProgressDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Update your progress")
            }

            AnimatedVisibility(
                visible = openProgressDialog,
                enter = slideInVertically() + expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn(initialAlpha = 0.3f),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                ProgressDialog(
                    book = book,
                    onDismiss = { openProgressDialog = false },
                    pagesInProgress = pagesInProgress,
                    bookViewModel = bookViewModel,
                    userId = userId
                )
            }

            val progress =
                if (book.pageCount > 0) pagesInProgress.toFloat() / book.pageCount else 0f

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(top = 16.dp),
            )

            Text(
                text = "${(progress * 100).toInt()}%",
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun GoodbooksAlertDialog(
    openDialog: Boolean,
    title: String,
    details: String,
    onDismiss: () -> Unit,
    onClick: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(5.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Warning,
                        contentDescription = "Warning"
                    )
                    Text(
                        title,
                        fontSize = 16.sp,
                        fontFamily = poppinsFamily,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Left
                    )
                }
            },
            text = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = details,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left
                )
            },
            confirmButton = {
                TextButton(onClick = onClick) {
                    Text(
                        "Remove",
                        fontSize = 16.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(
                        "Cancel",
                        fontSize = 16.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }
}

@Composable
fun GoodbooksButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface
        ),
    ) {
        Text(
            text = text,
            fontFamily = poppinsFamily,
            fontSize = 15.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ProgressDialog(
    book: Book,
    pagesInProgress: Int,
    onDismiss: () -> Unit,
    bookViewModel: BookViewModel,
    userId: String?
) {
    var pages by remember { mutableIntStateOf(pagesInProgress) }
    LaunchedEffect(pagesInProgress) {
        pages = pagesInProgress
    }

    Dialog(onDismissRequest = onDismiss) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        androidx.compose.material.IconButton(onClick = onDismiss) {
                            androidx.compose.material.Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.close),
                                tint = androidx.compose.material.MaterialTheme.colors.onSurface
                            )
                        }
                    },
                    title = {
                        androidx.compose.material.Text(
                            text = stringResource(id = R.string.label_progress),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = androidx.compose.material.MaterialTheme.typography.h6
                        )
                    },
                    actions = {
                        androidx.compose.material.IconButton(onClick =
                        {
                            CoroutineScope(Dispatchers.IO).launch {
                                bookViewModel.setProgressReading(userId, book.bookID, pages)
                            }
                            onDismiss()
                        })
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(id = R.string.close)
                            )
                        }
                    },
                    backgroundColor = androidx.compose.material.MaterialTheme.colors.surface,
                )
            },
            modifier = Modifier
                .width(300.dp)
                .height(500.dp)
        ) { innerPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                androidx.compose.material.Text(
                    text = "On Page",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                val keyboardController = LocalSoftwareKeyboardController.current
                val focusManager = LocalFocusManager.current

                OutlinedTextField(
                    value = pages.toString(),
                    onValueChange = { input ->
                        pages = if (input.isNotEmpty()) input.toInt() else 0
                    },
                    singleLine = true,
                    modifier = Modifier.width(75.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        })
                )

                Text(
                    text = "of ${book.pageCount}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ReviewDialog(
    book: Book,
    reviewText: String,
    onDismiss: () -> Unit,
    bookViewModel: BookViewModel,
    userId: String?
) {
    var review by remember { mutableStateOf(reviewText) }
    LaunchedEffect(reviewText) {
        review = reviewText
    }
    Dialog(onDismissRequest = onDismiss) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        androidx.compose.material.IconButton(onClick = onDismiss) {
                            androidx.compose.material.Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.close),
                                tint = androidx.compose.material.MaterialTheme.colors.onSurface
                            )
                        }
                    },
                    title = {
                        androidx.compose.material.Text(
                            text = stringResource(id = R.string.label_review),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = androidx.compose.material.MaterialTheme.typography.h6
                        )
                    },
                    actions = {
                        androidx.compose.material.IconButton(onClick =
                        {
                            CoroutineScope(Dispatchers.IO).launch {
                                bookViewModel.setReviewReading(userId, book.bookID, review)
                            }
                            onDismiss()
                        })
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(id = R.string.close)
                            )
                        }
                    },
                    backgroundColor = androidx.compose.material.MaterialTheme.colors.surface,
                )
            },
            modifier = Modifier
                .height(500.dp)
                .width(350.dp)
        ) { innerPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center

            ) {

                val keyboardController = LocalSoftwareKeyboardController.current
                val focusManager = LocalFocusManager.current

                OutlinedTextField(
                    value = review,
                    onValueChange = { review = it },
                    singleLine = false,
                    modifier = Modifier
                        .width(300.dp)
                        .height(200.dp)
                        .align(Alignment.CenterHorizontally),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                    label = { Text("Write a Review") }
                )
            }
        }
    }
}
