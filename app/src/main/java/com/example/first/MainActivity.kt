package com.example.first

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

/**
 * 应用程序的主Activity，负责初始化UI和导航
 *
 * 继承自ComponentActivity，使用Jetpack Compose构建UI
 * 包含应用程序的入口点onCreate方法
 */
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.first.navigation.Screen
import com.example.first.screens.*
import com.example.first.ui.theme.FirstTheme

class MainActivity : ComponentActivity() {
    /**
     * Activity创建时调用，初始化UI和设置内容视图
     *
     * @param savedInstanceState 保存的实例状态，可用于恢复Activity状态
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate started")
        enableEdgeToEdge()
        setContent {
            FirstTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
        Log.d("MainActivity", "onCreate completed")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
/**
 * 应用程序的主UI组件，包含导航和底部导航栏
 *
 * 使用Scaffold布局，包含：
 * - 底部导航栏
 * - 导航控制器
 * - 底部弹窗
 */
fun MainApp() {
    Log.d("MainApp", "MainApp started")
    val navController = rememberNavController()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            // 底部导航栏配置
            NavigationBar {
                // 获取当前导航状态
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // 首页导航项
                NavigationBarItem(
                    selected = currentRoute == Screen.Home.route,
                    onClick = { 
                        if (currentRoute != Screen.Home.route) {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "首页") },
                    label = { Text("首页") }
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.Profile.route,
                    onClick = { 
                        if (currentRoute != Screen.Profile.route) {
                            navController.navigate(Screen.Profile.route) {
                                popUpTo(Screen.Profile.route) { inclusive = true }
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "我的") },
                    label = { Text("我的") }
                )
            }
        }
    ) { paddingValues ->
        // 导航主机配置，管理所有可组合目的地的导航
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,  // 设置初始路由为首页
            modifier = Modifier.padding(paddingValues)
        ) {
            // 首页路由配置
            composable(route = Screen.Home.route) {
                HomeScreen(
                    onNavigateToDetail = { itemId -> 
                        navController.navigate(Screen.Detail.createRoute(itemId))
                    },
                    onShowBottomSheet = { showBottomSheet = true }
                )
            }
            
            // 详情页路由配置，包含itemId参数
            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("itemId") { type = NavType.StringType }  // 定义字符串类型的itemId参数
                )
            ) { backStackEntry ->  // 接收导航参数
                val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                DetailScreen(
                    itemId = itemId,
                    onBack = { navController.popBackStack() }
                )
            }
            
            composable(route = Screen.Profile.route) {
                ProfileScreen()
            }
        }

        // 底部弹窗显示逻辑
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false }  // 点击外部关闭弹窗
            ) {
                BottomSheetContent(
                    onDismiss = { showBottomSheet = false }
                )
            }
        }
    }
    Log.d("MainApp", "MainApp completed")
}