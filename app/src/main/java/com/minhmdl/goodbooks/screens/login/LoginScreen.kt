package com.minhmdl.goodbooks.screens.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel,
    dataStore: StoreUserName
) {

    var loading by remember { mutableStateOf(false) }

    viewModel.loading.observeForever {
        loading = it
    }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.book),
            contentDescription = "backgroundImage",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 15.dp)
                .size(200.dp),
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
                UserForm(
                    loading = loading,
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

@Composable
fun UserForm(
    navController: NavController,
    loading: Boolean,
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

