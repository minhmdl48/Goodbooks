package com.minhmdl.goodbooks.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name: String,
    val route: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
    )