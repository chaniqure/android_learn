package com.example.first.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.first.model.Item
import com.example.first.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "HomeViewModel"

/**
 * 首页数据状态
 */
sealed interface HomeUiState {
    data class Success(
        val items: List<Item>,
        val isLoading: Boolean = false,
        val hasMoreItems: Boolean = true,
        val currentPage: Int = 1
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
    object Loading : HomeUiState
}

/**
 * 首页 ViewModel
 * 负责管理首页数据的获取和状态维护
 */
class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    private val pageSize = 10 // 每页加载的数量

    init {
        Log.d(TAG, "初始化 ViewModel")
        loadItems(refresh = true)
    }

    fun loadItems(refresh: Boolean = false) {
        Log.d(TAG, "开始加载数据: refresh=$refresh")
        viewModelScope.launch {
            try {
                // 如果是刷新，显示加载状态；如果是加载更多，更新现有状态的 isLoading
                if (refresh) {
                    Log.d(TAG, "显示加载状态")
                    _uiState.value = HomeUiState.Loading
                } else {
                    val currentState = _uiState.value as? HomeUiState.Success ?: return@launch
                    if (currentState.isLoading || !currentState.hasMoreItems) {
                        Log.d(TAG, "跳过加载：isLoading=${currentState.isLoading}, hasMoreItems=${currentState.hasMoreItems}")
                        return@launch
                    }
                    Log.d(TAG, "更新加载状态")
                    _uiState.value = currentState.copy(isLoading = true)
                }

                val currentState = if (!refresh) _uiState.value as? HomeUiState.Success else null
                val currentPage = if (refresh) 1 else (currentState?.currentPage ?: 1)
                
                Log.d(TAG, "请求数据：page=$currentPage, size=$pageSize")
                val newItems = ApiService.api.getItemsByPage(
                    start = (currentPage - 1) * pageSize,
                    limit = pageSize
                )
                Log.d(TAG, "获取到 ${newItems.size} 条数据")
                
                val allItems = if (refresh) {
                    newItems
                } else {
                    currentState?.items.orEmpty() + newItems
                }

                Log.d(TAG, "更新状态：总条数=${allItems.size}, hasMore=${newItems.size >= pageSize}")
                _uiState.value = HomeUiState.Success(
                    items = allItems,
                    isLoading = false,
                    hasMoreItems = newItems.size >= pageSize,
                    currentPage = currentPage + 1
                )
            } catch (e: IOException) {
                Log.e(TAG, "网络错误", e)
                _uiState.value = HomeUiState.Error("网络连接失败，请检查网络后重试")
            } catch (e: HttpException) {
                Log.e(TAG, "HTTP错误: ${e.code()}", e)
                _uiState.value = HomeUiState.Error("服务器错误，请稍后重试")
            } catch (e: Exception) {
                Log.e(TAG, "未知错误", e)
                _uiState.value = HomeUiState.Error("未知错误：${e.message}")
            }
        }
    }

    fun loadMoreIfNeeded(lastVisibleItemIndex: Int) {
        val currentState = _uiState.value as? HomeUiState.Success ?: return
        val totalItems = currentState.items.size
        
        Log.d(TAG, "检查是否需要加载更多: lastIndex=$lastVisibleItemIndex, total=$totalItems")
        // 当最后可见项接近列表末尾时加载更多
        if (lastVisibleItemIndex >= totalItems - 2 && !currentState.isLoading && currentState.hasMoreItems) {
            Log.d(TAG, "触发加载更多")
            loadItems(refresh = false)
        }
    }

    fun addItem(title: String, description: String) {
        Log.d(TAG, "添加新项目: title=$title")
        viewModelScope.launch {
            try {
                val newItem = Item(
                    id = 0,  // ID 由服务器生成
                    title = title,
                    description = description
                )
                val addedItem = ApiService.api.addItem(newItem)
                Log.d(TAG, "新项目添加成功: id=${addedItem.id}")
                
                // 更新列表
                val currentState = _uiState.value as? HomeUiState.Success
                if (currentState != null) {
                    _uiState.value = currentState.copy(
                        items = listOf(addedItem) + currentState.items
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "添加项目失败", e)
                _uiState.value = HomeUiState.Error("添加项目失败：${e.message}")
                loadItems(refresh = true)
            }
        }
    }
} 