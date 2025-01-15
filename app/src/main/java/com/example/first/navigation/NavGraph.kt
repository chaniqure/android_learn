package com.example.first.navigation

// 定义导航路由
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: String) = "detail/$itemId"
    }
    object Profile : Screen("profile")
    object BottomSheet : Screen("bottomSheet")
} 