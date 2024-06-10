package com.minhmdl.goodbooks.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import com.minhmdl.goodbooks.utils.GoodbooksAlertDialog
import com.minhmdl.goodbooks.utils.GoodbooksButton
import com.minhmdl.goodbooks.utils.NavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = "Profile")
                })
        },
        bottomBar = {
            NavBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            GoodbooksButton(text = "Logout",
                onClick = {
                    /*TODO*/
                    isDialogOpen = true
                })
            GoodbooksAlertDialog(
                title = "Logout",
                details = "Are you sure you want to logout?",
                onDismiss = { isDialogOpen = false },
                onClick = {
                    /*TODO*/
                    isDialogOpen = false
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(GoodbooksDestinations.LOGIN_ROUTE)
                },
                openDialog = isDialogOpen
            )
        }

    }
}