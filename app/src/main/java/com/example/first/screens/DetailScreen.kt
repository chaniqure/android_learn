package com.example.first.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import com.example.first.model.Item
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    itemId: String,
    onBack: () -> Unit
) {
    // 模拟根据ID获取数据
    val item = remember {
        Item(
            itemId,
            "项目 $itemId",
            "这是项目 $itemId 的详细描述内容"
        )
    }

    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("详情页") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally() + fadeIn(),
            exit = slideOutHorizontally() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
} 