package com.example.first.model

import android.util.Log
import com.google.gson.annotations.SerializedName

private const val TAG = "Item"

/**
 * 表示应用中的项目实体
 *
 * 包含以下字段：
 * - id: 项目唯一标识
 * - title: 项目标题
 * - description: 项目描述
 * - imageUrl: 项目图片URL（不参与序列化）
 */
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
        /**
         * 根据项目ID生成图片URL
         *
         * @param id 项目ID
         * @return 图片URL，使用picsum.photos服务
         * @see <a href="https://picsum.photos">Picsum Photos</a>
         */
        private fun generateImageUrl(id: Int): String {
            // 使用 picsum.photos 的固定种子图片服务
            val imageUrl = "https://picsum.photos/id/${(id % 50 + 1)}/400/300"
            Log.d(TAG, "生成图片URL: $imageUrl")
            return imageUrl
        }
    }

    /**
     * 创建Item的副本
     *
     * @param id 新项目的ID，默认为当前项目的ID
     * @param title 新项目的标题，默认为当前项目的标题
     * @param description 新项目的描述，默认为当前项目的描述
     * @return 新的Item实例，imageUrl将根据新ID重新生成
     */
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