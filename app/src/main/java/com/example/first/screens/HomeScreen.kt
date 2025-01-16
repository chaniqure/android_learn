package com.example.first.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.first.model.Item

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit,
    onShowBottomSheet: () -> Unit
) {
    var items by remember {
        mutableStateOf(listOf(
            Item(
                id = "1",
                title = "示例项目1",
                description = "这是第一个示例项目的描述",
                imageUrl = "https://picsum.photos/300/200?random=1"
            ),
            Item(
                id = "2",
                title = "示例项目2",
                description = "这是第二个示例项目的描述",
                imageUrl = "https://picsum.photos/300/200?random=2"
            )
        ))
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                ItemCard(
                    item = item,
                    onItemClick = { onNavigateToDetail(item.id) }
                )
            }
        }

        // 底部按钮区域
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 添加新项目按钮
            Button(
                onClick = {
                    val newItem = Item(
                        id = "${items.size + 1}",
                        title = "新项目 ${items.size + 1}",
                        description = "这是新添加的项目描述",
                        imageUrl = "https://picsum.photos/300/200?random=${items.size + 1}"
                    )
                    items = items + newItem
                }
            ) {
                Text("添加新项目")
            }

            // 显示底部弹出框按钮
            Button(
                onClick = onShowBottomSheet
            ) {
                Text("显示底部弹出框")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemCard(
    item: Item,
    onItemClick: () -> Unit
) {
    Card(
        onClick = onItemClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // 图片
            Image(
                painter = rememberAsyncImagePainter(item.imageUrl),
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            
            // 内容
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
} 