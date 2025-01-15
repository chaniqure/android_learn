package com.example.first

import android.os.Bundle
import androidx.activity.ComponentActivity
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainApp() {
    // 创建导航控制器
    val navController = rememberNavController()
    
    // 创建底部弹出框状态
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        // 底部导航栏
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

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
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(
                route = Screen.Home.route,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() }
            ) {
                HomeScreen(
                    onNavigateToDetail = { itemId -> 
                        navController.navigate(Screen.Detail.createRoute(itemId))
                    },
                    onShowBottomSheet = { showBottomSheet = true }
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("itemId") { type = NavType.StringType }
                ),
                enterTransition = { slideInHorizontally() + fadeIn() },
                exitTransition = { slideOutHorizontally() + fadeOut() }
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                DetailScreen(
                    itemId = itemId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.Profile.route,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() }
            ) {
                ProfileScreen()
            }
        }

        // 显示底部弹出框
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState
            ) {
                BottomSheetContent(
                    onDismiss = { showBottomSheet = false }
                )
            }
        }
    }
}