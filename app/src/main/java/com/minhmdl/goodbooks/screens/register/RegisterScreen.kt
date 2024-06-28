package com.minhmdl.goodbooks.screens.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grayseal.bookshelf.ui.theme.poppinsFamily
import com.minhmdl.goodbooks.data.StoreUserName
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import com.minhmdl.goodbooks.ui.theme.Black
import com.minhmdl.goodbooks.ui.theme.Gray500
import com.minhmdl.goodbooks.utils.EmailInput
import com.minhmdl.goodbooks.utils.NameInput
import com.minhmdl.goodbooks.utils.PasswordInput
import com.minhmdl.goodbooks.utils.SubmitButton
import com.minhmdl.goodbooks.utils.isValidEmail
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavController, viewModel: RegisterViewModel, dataStore: StoreUserName
) {

    var loading by remember { mutableStateOf(false) }

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
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier.fillMaxHeight(0.6f),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                val text = "Create an account"
                Text(
                    text,
                    fontFamily = poppinsFamily,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                    color = Black
                )
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    CreateUserForm(
                        dataStore = dataStore,
                        loading = loading,
                        navController = navController,
                    ) { email, password ->
                        viewModel.createUserWithEmailAndPassword(email,
                            password,
                            onRegisterSuccess = { navController.navigate(GoodbooksDestinations.LOGIN_ROUTE) },
                            onError = {
                                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun CreateUserForm(
    navController: NavController,
    dataStore: StoreUserName,
    loading: Boolean,
    onDone: (String, String) -> Unit = { _, _ -> }
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val name = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val passwordFocusRequest = FocusRequester.Default

    val valid: Boolean = remember(email.value, password.value) {
        (email.value.trim().isNotEmpty() && password.value.trim()
            .isNotEmpty() && password.value.length >= 6) && isValidEmail(email.value)
    }

    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            NameInput(nameState = name, enabled = !loading)

            EmailInput(emailState = email, enabled = !loading)

            PasswordInput(
                modifier = Modifier.focusRequester(passwordFocusRequest),
                passwordState = password,
                labelId = "Password",
                enabled = !loading,
                passwordVisibility = passwordVisibility,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Gray500, fontSize = 12.sp, fontFamily = poppinsFamily
                        )
                    ) {
                        append("By signing up, you agree to the Goodbooks ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue, fontSize = 12.sp, fontFamily = poppinsFamily
                        )
                    ) {
                        append("Terms of Service ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Gray500, fontSize = 12.sp, fontFamily = poppinsFamily
                        )
                    ) {
                        append("and ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue, fontSize = 12.sp, fontFamily = poppinsFamily
                        )
                    ) {
                        append("Privacy Policy.")
                    }
                })
            }

            SubmitButton(
                textId = "Sign up", loading = loading, validInputs = valid
            ) {
                onDone(email.value.trim(), password.value.trim())
                name.value.trim()
                scope.launch {
                    dataStore.saveName(name.value)
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                val text = "Sign In"
                val desc = "Already have an account?"
                Text(
                    text = desc, fontFamily = poppinsFamily, fontSize = 15.sp, color = Black
                )
                Text(text,
                    fontFamily = poppinsFamily,
                    fontSize = 15.sp,
                    color = Black,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(GoodbooksDestinations.LOGIN_ROUTE) {
                                launchSingleTop = true
                            }
                        }
                        .padding(start = 5.dp),
                    fontWeight = FontWeight.Bold)
            }
        }
    }

}