package com.minhmdl.goodbooks.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.InsertEmoticon
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.grayseal.bookshelf.ui.theme.poppinsFamily
import com.minhmdl.goodbooks.R
import com.minhmdl.goodbooks.navigation.BottomNavItem
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import com.minhmdl.goodbooks.ui.theme.Black
import com.minhmdl.goodbooks.ui.theme.Gray200
import com.minhmdl.goodbooks.ui.theme.Gray500
import com.minhmdl.goodbooks.ui.theme.Green
import com.minhmdl.goodbooks.ui.theme.Pink500
import com.minhmdl.goodbooks.ui.theme.Yellow
import com.minhmdl.goodbooks.ui.theme.iconColor


/**
EmailInput is a composable function that creates an email input field.
 */
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

/**
NameInput is a composable function that creates an input field for a user's name.
 */

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

/**
PasswordInput is a composable function that creates a password input field.
 */

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

/**
PasswordVisibility is a composable function that creates a button to toggle the visibility of the password input field.
 */
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

/**
EmailInputField is a composable function that creates an input field for a user's email address.
 */

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

/**
SubmitButton is a composable function that creates a button for submitting a form.
 */
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


/**
A Composable function that displays a category with an image and a text label.
 */

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
                .background(color = MaterialTheme.colorScheme.secondary, shape = CircleShape)
                .clickable(onClick = onClick),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondary
        ) {
            Image(
                painter = painterResource(id = image),
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

/**
A Composable function that displays a book with its author and image.

The book is displayed as a Surface with a rounded corner shape, with the provided image inside it.
The book's title and author are displayed below the image. When the book is clicked, the onClick
function is called.
// */
@Composable
fun Reading(
    genre: String,
    bookAuthor: String,
    bookTitle: String,
    imageUrl: String,
    rating: String,
    onClick: () -> Unit
) {
    var loading by remember {
        mutableStateOf(false)
    }
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        shape = RoundedCornerShape(5.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build(),
                contentDescription = "Book Image",
                contentScale = ContentScale.Inside,
                onLoading = {
                    loading = true
                },
                onSuccess = {
                    loading = false
                }
            )
            if (loading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(25.dp),
                        color = Yellow
                    )
                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    genre,
                    fontFamily = poppinsFamily,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    bookTitle,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    "by $bookAuthor",
                    fontFamily = poppinsFamily,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row {
                            for (i in 0 until rating.toFloat().toInt()) {
                                androidx.compose.material.Icon(
                                    Icons.Rounded.Star,
                                    contentDescription = "star",
                                    tint = Yellow,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            if ((5 - rating.toFloat()) > 0) {
                                val unrated = 5 - rating.toFloat().toInt()
                                if ((rating.toFloat() - rating.toFloat().toInt()) > 0) {
                                    androidx.compose.material.Icon(
                                        Icons.Rounded.StarHalf,
                                        contentDescription = "star",
                                        tint = Yellow,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    for (i in 0 until unrated - 1) {
                                        androidx.compose.material.Icon(
                                            Icons.Rounded.Star,
                                            contentDescription = "star",
                                            tint = Color.LightGray,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                } else {
                                    for (i in 0 until unrated) {
                                        androidx.compose.material.Icon(
                                            Icons.Rounded.Star,
                                            contentDescription = "star",
                                            tint = Color.LightGray,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.End)
                                .clickable(onClick = onClick),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Read more",
                                fontFamily = poppinsFamily,
                                fontSize = 12.sp,
                                color = Pink500,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            androidx.compose.material.Icon(
                                Icons.Rounded.ArrowForward,
                                contentDescription = "Arrow",
                                tint = Pink500,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
A Composable function that displays a navigation bar with clickable icons and labels.
The navigation bar consists of four items: Home, Shelves, Favourites, and Reviews, each with an
associated icon. The selected item is highlighted with the primary color, while the unselected
items are displayed with the default Material Design colors. When an item is clicked, the
selectedItem variable is updated to reflect the new selection.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(
            name = "Home",
            route = GoodbooksDestinations.HOME_ROUTE,
            icon = R.drawable.home,
        ),
        BottomNavItem(
            name = "My Books",
            route = GoodbooksDestinations.DETAIL_ROUTE,
            icon = R.drawable.shelves,
         ),

    )

    val backStackEntry = navController.currentBackStackEntryAsState()

    // Extract colors from MaterialTheme.colorScheme
    val primaryColor = Black
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val surfaceColor = MaterialTheme.colorScheme.surface

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp,
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.name,
                        modifier = Modifier
                            .size(30.dp)
                            .background(color = Color.Transparent)
                    )
                },
                label = {
                    Text(
                        item.name, fontFamily = poppinsFamily,
                        fontSize = 12.sp,
                    )
                },
                selected = selected,
                onClick = {
                    when (item.name) {
                        "Home" -> navController.navigate(GoodbooksDestinations.HOME_ROUTE)
                        "My Books" -> navController.navigate(GoodbooksDestinations.SHELF_ROUTE)
                        "Favourites" -> navController.navigate(GoodbooksDestinations.FAVOURITE_ROUTE)
                        "Reviews" -> navController.navigate(GoodbooksDestinations.REVIEW_ROUTE)
                    }
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = Black,
                    selectedTextColor = Black,
                    unselectedTextColor = onSurfaceColor,
                    unselectedIconColor = onSurfaceColor,
                    disabledIconColor = Color.Gray,
                    disabledTextColor = Color.Gray,
                    selectedIndicatorColor = surfaceColor

                ),
                interactionSource = MutableInteractionSource()
            )
        }
    }
}

@Composable
fun SearchInputField(
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean,
    keyBoardType: KeyboardType = KeyboardType.Ascii,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {
            valueState.value = it
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search",
                modifier = Modifier
                    .size(30.dp)
                    .background(color = Color.Transparent),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
            )
        },
        trailingIcon = {
                       IconButton(onClick = { valueState.value = ""}){
                           Icon(
                               imageVector = Icons.Rounded.Close,
                               contentDescription = "Clear",
                               tint = MaterialTheme.colorScheme.onBackground
                           )
                       }
        },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 15.sp, fontFamily = poppinsFamily),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyBoardType, imeAction = imeAction),
        keyboardActions = onAction,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        placeholder = { Text(text = labelId, fontFamily = poppinsFamily, fontSize = 15.sp) },
        shape = RoundedCornerShape(11.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = Black,
            focusedBorderColor = Black,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            focusedLeadingIconColor = Black,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
            focusedPlaceholderColor = Gray500,
            selectionColors = TextSelectionColors(
                handleColor = Black,
                backgroundColor = Green
            )

        )
    )
}

/*
A composable function that displays a search card with book details.
The card contains an image, book title, author, and a preview text.
Clicking the card triggers the onClick function.
 */

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
                    .offset(y= (-10).dp),
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

@Preview
@Composable
fun BookListItemPreview() {
    BookListItem(
        bookTitle = "The Complete sherlock Holmes: Volume II",
        bookAuthor = "F. Scott Fitzgerald",
        imageUrl = "https://m.media-amazon.com/images/I/81QuEGw8VPL._AC_UF1000,1000_QL80_.jpg",
        onClick = {}
    )
}

/**
 * A composable function that displays an alert dialog with a title, details, a confirm button,
 * and a cancel button.
 */
@Composable
fun ShelvesAlertDialog(
    openDialog: Boolean,
    title: String,
    details: String,
    drawable: Int,
    color: Color = Pink500,
    size: Dp = 30.dp,
    onDismiss: () -> Unit,
    onClick: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(5.dp),
            containerColor = MaterialTheme.colorScheme.background,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.info),
                        contentDescription = "Info",
                        modifier = Modifier
                            .size(size)
                            .align(Alignment.CenterVertically),
                        colorFilter = ColorFilter.tint(color)
                    )
                    Text(
                        title,
                        fontSize = 16.sp,
                        fontFamily = poppinsFamily,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Left
                    )
                }
            },
            text = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = details,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 13.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Left
                )
            },
            confirmButton = {
                TextButton(onClick = onClick) {
                    Text(
                        "Confirm",
                        fontSize = 16.sp,
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
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
                        color = Black
                    )
                }
            }
        )
    }
}