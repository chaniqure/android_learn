package com.example.first.network

import android.util.Log
import com.example.first.model.Item
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private const val TAG = "ApiService"

/**
 * API 接口定义
 * 使用 JSONPlaceholder 提供的免费 API 服务
 * https://jsonplaceholder.typicode.com/
 */
interface ApiInterface {
    @GET("posts")
    suspend fun getItems(): List<Item>

    @GET("posts")
    suspend fun getItemsByPage(
        @Query("_start") start: Int,
        @Query("_limit") limit: Int
    ): List<Item>

    @POST("posts")
    suspend fun addItem(@Body item: Item): Item

    @GET("posts/{id}")
    suspend fun getItem(@Path("id") id: Int): Item
}

/**
 * API 服务单例
 */
object ApiService {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    // 创建日志拦截器
    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        when {
            message.startsWith("-->") -> Log.d(TAG, "发送请求: $message")
            message.startsWith("<--") -> Log.d(TAG, "收到响应: $message")
            message.startsWith("{") || message.startsWith("[") -> Log.d(TAG, "响应数据: $message")
            else -> Log.v(TAG, "其他信息: $message")
        }
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 创建网络请求拦截器
    private val networkInterceptor = { chain: okhttp3.Interceptor.Chain ->
        val request = chain.request()
        Log.d(TAG, "请求URL: ${request.url}")
        val response = chain.proceed(request)
        Log.d(TAG, "响应码: ${response.code}")
        response
    }

    // 创建 OkHttpClient，添加日志拦截器和超时设置
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor(networkInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiInterface = retrofit.create(ApiInterface::class.java)
} 