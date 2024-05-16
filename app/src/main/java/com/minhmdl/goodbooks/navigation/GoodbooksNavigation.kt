package com.minhmdl.goodbooks.navigation

import com.minhmdl.goodbooks.navigation.GoodbooksScreens.CATEGORY_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.DETAIL_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.FAVOURITE_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.HOME_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.LOGIN_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.REVIEW_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.SEARCH_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.SHELF_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.SPLASH_SCREEN

/**
 * Screens used in [GoodbooksDestinations]
 */

private object GoodbooksScreens{
    const val SPLASH_SCREEN = "splash"
    const val ONBOARD_SCREEN = "onboard"
    const val HOME_SCREEN= "home"
    const val SEARCH_SCREEN= "search"
    const val DETAIL_SCREEN= "detail"
    const val SHELF_SCREEN= "shelf"
    const val CATEGORY_SCREEN= "category"
    const val FAVOURITE_SCREEN= "favourite"
    const val REVIEW_SCREEN = "review"
    const val LOGIN_SCREEN = "login"
}

/**
 * Arguments used in [GoodbooksDestinations] routes
 */

object GoodbooksDestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val BOOK_ID_ARG = "bookId"
    const val TITLE_ARG = "title"
}

/**
 * Destinations used in the [MainActivity]
 */
object GoodbooksDestinations {
    const val SPLASH_ROUTE = SPLASH_SCREEN
    const val HOME_ROUTE = HOME_SCREEN
    const val SEARCH_ROUTE = SEARCH_SCREEN
    const val DETAIL_ROUTE = DETAIL_SCREEN
    const val SHELF_ROUTE = SHELF_SCREEN
    const val CATEGORY_ROUTE = CATEGORY_SCREEN
    const val FAVOURITE_ROUTE = FAVOURITE_SCREEN
    const val REVIEW_ROUTE = REVIEW_SCREEN
    const val LOGIN_ROUTE = LOGIN_SCREEN
    const val REGISTER_ROUTE= "register"
    const val FORGOT_PASSWORD_ROUTE = "forgotPassword"
    const val USER_ROUTE= "user"
}

/**
 * Models the navigation actions in the app.
 */
//class GoodbooksNavigationActions(private val navController: NavHostController) {
//
//    fun navigateToHome() {
//        navController.navigate(GoodbooksDestinations.HOME_ROUTE) {
//            // Pop up to the start destination of the graph to
//            // avoid building up a large stack of destinations
//            // on the back stack as users select items
//            popUpTo(navController.graph.startDestinationRoute!!) {
//                inclusive = true
//                saveState = true
//            }
//            // Avoid multiple copies of the same destination when
//            // reselecting the same item
//            launchSingleTop = true
//            // Restore state when reselecting a previously selected item
//            restoreState = true
//        }
//    }
//
//}
