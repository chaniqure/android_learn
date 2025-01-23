package com.example.first.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 底部弹出框内容 Composable 函数。
 *
 * 此 Composable 函数用于展示底部弹出框的内容，
 * 包含一个标题和一个关闭按钮。
 *
 * @param onDismiss 点击关闭按钮时触发的回调函数。
 */
@Composable
fun BottomSheetContent(
    onDismiss: () -> Unit // 关闭底部弹出框的回调函数
) {
    Column(
        modifier = Modifier
            .fillMaxWidth() // 填充父容器的宽度
            .padding(16.dp) // 添加内边距
    ) {
        Text(
            text = "底部弹出框内容", // 文本内容
            style = MaterialTheme.typography.headlineMedium // 使用 MaterialTheme 的 headlineMedium 样式
        )

        Spacer(modifier = Modifier.height(16.dp)) // 添加一个 16dp 高度的 Spacer

        Button(onClick = onDismiss) { // 按钮，点击时调用 onDismiss 回调函数
            Text("关闭") // 按钮上的文本
        }
    }
}