package com.example.first.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetContent(onDismiss: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "底部弹出框",
                style = MaterialTheme.typography.headlineSmall
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("这是底部弹出框的内容")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("关闭")
            }
        }
    }
} 