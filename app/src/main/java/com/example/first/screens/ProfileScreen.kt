package com.example.first.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "个人中心",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 示例个人信息项
        ListItem(
            headlineContent = { Text("用户名") },
            supportingContent = { Text("张三") }
        )
        
        ListItem(
            headlineContent = { Text("邮箱") },
            supportingContent = { Text("zhangsan@example.com") }
        )
    }
} 