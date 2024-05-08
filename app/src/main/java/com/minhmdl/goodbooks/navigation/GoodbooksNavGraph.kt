package com.minhmdl.goodbooks.navigation


import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DrawerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.minhmdl.goodbooks.data.StoreUserName
import com.minhmdl.goodbooks.screens.SplashScreen
import com.minhmdl.goodbooks.screens.book.BookScreen
import com.minhmdl.goodbooks.screens.book.BookViewModel
import com.minhmdl.goodbooks.screens.category.CategoryScreen
import com.minhmdl.goodbooks.screens.home.HomeScreen
import com.minhmdl.goodbooks.screens.home.HomeViewModel
import com.minhmdl.goodbooks.screens.login.LoginScreen
import com.minhmdl.goodbooks.screens.login.LoginViewModel
import com.minhmdl.goodbooks.screens.register.RegisterScreen
import com.minhmdl.goodbooks.screens.register.RegisterViewModel
import com.minhmdl.goodbooks.screens.search.SearchScreen
import com.minhmdl.goodbooks.screens.search.SearchViewModel
import com.minhmdl.goodbooks.screens.shelf.ShelfViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.CoroutineScope


@Composable
fun GoodbooksNavGraph() {
    val navController: NavHostController = rememberNavController()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val startDestination: String = GoodbooksDestinations.SPLASH_ROUTE
    val navActions: GoodbooksNavigationActions = remember(navController) {
        GoodbooksNavigationActions(navController)
    }
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val searchViewModel: SearchViewModel = viewModel()
    val shelfViewModel: ShelfViewModel = viewModel()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination
    val loginViewModel: LoginViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()

    val context = LocalContext.current
    val registerViewModel: RegisterViewModel = viewModel()
    val dataStore: StoreUserName = StoreUserName(context)
    NavHost(navController = navController, startDestination = GoodbooksDestinations.HOME_ROUTE) {
        composable(GoodbooksDestinations.SPLASH_ROUTE) {
            SplashScreen(navController = navController)
        }
        composable(GoodbooksDestinations.REGISTER_ROUTE){
            RegisterScreen(navController = navController, viewModel = registerViewModel, dataStore = dataStore)
        }
        composable(GoodbooksDestinations.LOGIN_ROUTE){
            LoginScreen(navController = navController, viewModel = loginViewModel, dataStore = dataStore)
        }
        composable(GoodbooksDestinations.HOME_ROUTE) {
            HomeScreen(navController = navController, loginViewModel=loginViewModel, homeViewModel = homeViewModel, searchViewModel = searchViewModel)
        }

        val bookRoute = GoodbooksDestinations.DETAIL_ROUTE
        composable("$bookRoute/{bookId}", arguments = listOf(navArgument(name = "bookId") {
            type = NavType.StringType})) { navBack ->
            navBack.arguments?.getString("bookId").let { bookId ->
                BookScreen(
                    navController = navController,
                    bookViewModel = bookViewModel,
                    bookId = bookId,
                    shelfViewModel = shelfViewModel
                )
            }
        }
        composable(GoodbooksDestinations.SEARCH_ROUTE) {
            SearchScreen(navController = navController, searchViewModel)
        }

        val route = GoodbooksDestinations.CATEGORY_ROUTE
        composable("$route/{query}", arguments = listOf(navArgument(name = "query") {
            type = NavType.StringType
        })) { navBack ->
            navBack.arguments?.getString("query").let { query ->
                CategoryScreen(navController = navController, searchViewModel, category = query)
            }
        }
    }
}


