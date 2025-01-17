package com.example.first.model

import android.util.Log
import com.google.gson.annotations.SerializedName

private const val TAG = "Item"

data class Item(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("body")
    val description: String,
    
    @Transient // 标记这个字段不参与序列化
    val imageUrl: String = generateImageUrl(id) // 使用函数生成图片URL
) {
    companion object {
        private fun generateImageUrl(id: Int): String {
            // 使用 picsum.photos 的固定种子图片服务
            val imageUrl = "https://picsum.photos/id/${(id % 50 + 1)}/400/300"
            Log.d(TAG, "生成图片URL: $imageUrl")
            return imageUrl
        }
    }

    // 自定义 copy 方法，确保 imageUrl 总是基于 id 生成
    fun copy(
        id: Int = this.id,
        title: String = this.title,
        description: String = this.description
    ) = Item(
        id = id,
        title = title,
        description = description
    )
} 