package com.minhmdl.goodbooks.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.minhmdl.goodbooks.data.StoreUserName
import com.minhmdl.goodbooks.screens.ProfileScreen
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
import com.minhmdl.goodbooks.screens.shelf.ShelfScreen
import com.minhmdl.goodbooks.screens.shelf.ShelfViewModel


@Composable
fun GoodbooksNavGraph() {

    val navController: NavHostController = rememberNavController()

    val searchViewModel: SearchViewModel = viewModel()
    val shelfViewModel: ShelfViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()

    val context = LocalContext.current
    val registerViewModel: RegisterViewModel = viewModel()
    val dataStore: StoreUserName = StoreUserName(context)
    NavHost(navController = navController, startDestination = GoodbooksDestinations.SPLASH_ROUTE) {
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
        composable(GoodbooksDestinations.SHELF_ROUTE){
            ShelfScreen(navController = navController, shelfViewModel = shelfViewModel)
        }
        val bookRoute = GoodbooksDestinations.DETAIL_ROUTE
        composable("$bookRoute/{bookId}", arguments = listOf(navArgument(name = "bookId") {
            type = NavType.StringType})) { navBack ->
            navBack.arguments?.getString("bookId").let { bookId ->
                BookScreen(
                    navController = navController,
                    bookViewModel = bookViewModel,
                    bookId = bookId
                )
            }
        }
        composable(GoodbooksDestinations.SEARCH_ROUTE) {
            SearchScreen(navController = navController, searchViewModel)
        }
        composable(GoodbooksDestinations.PROFILE_ROUTE){
            ProfileScreen(navController = navController)
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


