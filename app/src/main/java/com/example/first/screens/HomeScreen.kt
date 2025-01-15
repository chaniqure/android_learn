package com.example.first.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.first.model.Item

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit,
    onShowBottomSheet: () -> Unit
) {
    var items by remember {
        mutableStateOf(listOf(
            Item("1", "第一项", "这是第一项的详细描述"),
            Item("2", "第二项", "这是第二项的详细描述"),
            Item("3", "第三项", "这是第三项的详细描述")
        ))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "首页",
            style = MaterialTheme.typography.headlineMedium
        )

        // 添加新项目的按钮
        Button(
            onClick = {
                val newItem = Item(
                    "${items.size + 1}",
                    "新项目 ${items.size + 1}",
                    "这是新添加的项目描述"
                )
                items = items + newItem
            }
        ) {
            Text("添加新项目")
        }

        // 带动画效果的列表
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items, key = { it.id }) { item ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToDetail(item.id) }
                            .animateContentSize()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = onShowBottomSheet,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("显示底部弹出框")
        }
    }
} 