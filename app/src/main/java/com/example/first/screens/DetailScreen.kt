package com.example.first.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.first.model.Item
import com.example.first.viewmodel.DetailUiState
import com.example.first.viewmodel.DetailViewModel
import kotlinx.coroutines.launch

/**
 * 详情页面
 * 这是一个展示单个项目详细信息的页面，包含以下功能：
 * 1. 顶部应用栏，带有返回按钮
 * 2. 图片展示区域
 * 3. 标题和描述信息
 * 4. 交互按钮（收藏、分享等）
 * 5. 底部操作区域
 *
 * @param itemId 要显示的项目ID
 * @param onBack 返回按钮点击回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    itemId: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    var isFavorite by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    // 加载数据
    LaunchedEffect(itemId) {
        viewModel.loadItem(itemId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("项目详情") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "返回")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("分享功能已触发")
                            }
                        }) {
                            Icon(Icons.Default.Share, "分享")
                        }
                        IconButton(onClick = {
                            isFavorite = !isFavorite
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    if (isFavorite) "已添加到收藏" else "已取消收藏"
                                )
                            }
                        }) {
                            Icon(
                                if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                "收藏"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                Surface(
                    tonalElevation = 3.dp,
                    shadowElevation = 3.dp
                ) {
                    Column {
                        Divider(color = MaterialTheme.colorScheme.outlineVariant)
                        BottomAppBar(
                            modifier = Modifier.height(80.dp),
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("评论功能已触发")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                ) {
                                    Icon(Icons.Default.Comment, "评论")
                                    Spacer(Modifier.width(8.dp))
                                    Text("评论")
                                }
                                Button(
                                    onClick = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("点赞功能已触发")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                ) {
                                    Icon(Icons.Default.ThumbUp, "点赞")
                                    Spacer(Modifier.width(8.dp))
                                    Text("点赞")
                                }
                            }
                        }
                        Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (uiState) {
                    is DetailUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is DetailUiState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = (uiState as DetailUiState.Error).message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadItem(itemId) }) {
                                Text("重试")
                            }
                        }
                    }
                    is DetailUiState.Success -> {
                        val item = (uiState as DetailUiState.Success).item
                        DetailContent(item)
                    }
                }
            }
        }
    }
}

/**
 * 详情页内容区域
 * 展示项目的图片、标题和描述等详细信息
 *
 * @param item 要显示的项目数据
 */
@Composable
private fun DetailContent(item: Item) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 图片区域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = "项目图片",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                error = ColorPainter(MaterialTheme.colorScheme.errorContainer)
            )
        }

        // 内容区域
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 标签区域
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestionChip(
                    onClick = { /* 标签点击事件 */ },
                    label = { Text("文章 #${item.id}") }
                )
                SuggestionChip(
                    onClick = { /* 标签点击事件 */ },
                    label = { Text("JSONPlaceholder") }
                )
            }
        }
    }
} 