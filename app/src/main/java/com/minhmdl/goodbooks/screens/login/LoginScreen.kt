package com.minhmdl.goodbooks.screens.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grayseal.bookshelf.ui.theme.poppinsFamily
import com.minhmdl.goodbooks.R
import com.minhmdl.goodbooks.data.StoreUserName
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import com.minhmdl.goodbooks.ui.theme.Black
import com.minhmdl.goodbooks.utils.EmailInput
import com.minhmdl.goodbooks.utils.PasswordInput
import com.minhmdl.goodbooks.utils.SubmitButton
import com.minhmdl.goodbooks.utils.isValidEmail

/**
Composable function to display a login screen.
 * @param navController The NavController used to navigate to different screens.
 * @param viewModel The viewModel containing logic for login and creating a user.
 * @param dataStore The dataStore used to store and retrieve user information.
 */
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel,
    dataStore: StoreUserName
) {

    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }

    // Create a local mutable state to hold the value of the loading flag
    var loading by remember { mutableStateOf(false) }

    // Observe the value of the loading LiveData and update the local state accordingly
    viewModel.loading.observeForever {
        loading = it
    }
    val context = LocalContext.current
Column(
   modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.book),
            contentDescription = "backgroundImage",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 15.dp)
                .size(200.dp), // specify the size you want
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            val text = "LOGIN"
            Text(
                text,
                fontFamily = poppinsFamily,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp),
                color = Color.Black
            )
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                    .clip(RoundedCornerShape(10.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                if (showLoginForm.value) UserForm(
                    showLoginForm = showLoginForm,
                    dataStore = dataStore,
                    loading = loading,
                    isCreateAccount = false,
                    navController = navController
                ) { email, password ->
                    viewModel.signInWithEmailAndPassword(email, password,
                        onLoginSuccess = { navController.navigate(GoodbooksDestinations.HOME_ROUTE) },
                        onError = {
                            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }
        }
    }
}
}

/**
Composable function to display a user form for login or account creation.
 * @param showLoginForm A MutableState that holds a boolean indicating whether the login form should be shown or not.
 * @param dataStore The dataStore used to store and retrieve user information.
 * @param loading A boolean indicating whether the form is in loading state or not.
 * @param isCreateAccount A boolean indicating whether the form is for account creation or login.
 * @param onDone A function to be called when the form is submitted, takes in email and password as arguments.
 */
@Composable
fun UserForm(
    showLoginForm: MutableState<Boolean>,
    dataStore: StoreUserName,
    navController: NavController,
    loading: Boolean,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, password -> }
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val passwordFocusRequest = FocusRequester.Default

    val valid: Boolean = remember(email.value, password.value) {
            (email.value.trim().isNotEmpty()
                    && password.value.trim().isNotEmpty()
                    && password.value.length >= 6) && isValidEmail(email.value)
        }

    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EmailInput(emailState = email, enabled = !loading)

            PasswordInput(
                modifier = Modifier.focusRequester(passwordFocusRequest),
                passwordState = password,
                labelId = "Password",
                enabled = !loading,
                passwordVisibility = passwordVisibility,
            )

            SubmitButton(
                textId = "Log in",
                loading = loading,
                validInputs = valid
            ) { onDone(email.value.trim(), password.value.trim()) }

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                    Row(
                    modifier = Modifier.padding(top = 8.dp, bottom = 50.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val text = "Sign up now!!"
                    val desc = "Don't have an account?"
                    Text(
                        text = desc,
                        fontFamily = poppinsFamily,
                        fontSize = 15.sp,
                        color = Black
                    )
                    Text(text,
                        fontFamily = poppinsFamily,
                        fontSize = 15.sp,
                        color = Black,
                        modifier = Modifier
                            .clickable {
                                navController.navigate(GoodbooksDestinations.REGISTER_ROUTE) {
                                    // This will clear the backstack and make the RegisterScreen the top of the navigation stack
                                    launchSingleTop = true
                                }
                            }
                            .padding(start = 5.dp),
                        fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}



