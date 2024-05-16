package com.minhmdl.goodbooks.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.minhmdl.goodbooks.R
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import kotlinx.coroutines.delay

/**
A composable function that represents a splash screen in the app.
 * The splash screen is displayed while the app is starting up, and is responsible for animating
 * the logo image and transitioning to the HomeScreen. The animation is triggered when the function
 * is called, and lasts for 2 seconds before transitioning to the HomeScreen. This function uses the
 * navController to navigate between screens.
 * @param navController The NavController used to navigate between screens.
 */
@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAnimation = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000L)
        navController.popBackStack()
        navController.navigate(GoodbooksDestinations.HOME_ROUTE)
    }
    Surface {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.alpha(alphaAnimation.value)
                    .width(150.dp)
                    .height(150.dp),
                painter = painterResource(id = R.drawable.book),
                contentDescription = "Splash Book",
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    val navController = rememberNavController()
    SplashScreen(navController = navController)
}
