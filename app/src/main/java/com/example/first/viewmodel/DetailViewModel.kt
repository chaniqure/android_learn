package com.example.first.viewmodel

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

/**
 * 详情页面状态
 */
sealed interface DetailUiState {
    data class Success(val item: Item) : DetailUiState
    data class Error(val message: String) : DetailUiState
    object Loading : DetailUiState
}

/**
 * 详情页面 ViewModel
 */
class DetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                val id = itemId.toIntOrNull()
                if (id == null) {
                    _uiState.value = DetailUiState.Error("无效的项目ID")
                    return@launch
                }
                
                val item = ApiService.api.getItem(id)
                _uiState.value = DetailUiState.Success(item)
            } catch (e: IOException) {
                _uiState.value = DetailUiState.Error("网络连接失败，请检查网络后重试")
            } catch (e: HttpException) {
                _uiState.value = DetailUiState.Error("服务器错误，请稍后重试")
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error("未知错误：${e.message}")
            }
        }
    }
} 