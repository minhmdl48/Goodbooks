package com.minhmdl.goodbooks.navigation

import com.minhmdl.goodbooks.navigation.GoodbooksScreens.CATEGORY_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.DETAIL_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.FAVOURITE_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.HOME_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.LOGIN_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.PROFILE_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.REVIEW_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.SEARCH_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.SHELF_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.SPLASH_SCREEN
import com.minhmdl.goodbooks.navigation.GoodbooksScreens.STATS_SCREEN

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
    const val PROFILE_SCREEN = "profile"
    const val STATS_SCREEN = "statistics"
}

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
    const val PROFILE_ROUTE = PROFILE_SCREEN
    const val FORGOT_PASSWORD_ROUTE = "forgotPassword"
    const val USER_ROUTE= "user"
    const val STATS_ROUTE = STATS_SCREEN
}

