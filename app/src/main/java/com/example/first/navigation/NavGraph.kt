package com.example.first.navigation

/**
 * 应用程序的导航路由定义
 * 使用sealed class确保导航路由的类型安全
 */
sealed class Screen(val route: String) {
    /**
     * 首页路由
     */
    object Home : Screen("home")

    /**
     * 详情页路由
     * 包含itemId参数用于标识具体项目
     */
    object Detail : Screen("detail/{itemId}") {
        /**
         * 创建带有具体itemId的详情页路由
         * @param itemId 项目ID
         * @return 完整的详情页路由字符串
         */
        fun createRoute(itemId: String) = "detail/$itemId"
    }

    /**
     * 个人资料页路由
     */
    object Profile : Screen("profile")

    /**
     * 底部弹窗路由
     */
    object BottomSheet : Screen("bottomSheet")
} 