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
    /**
     * 获取所有项目列表
     *
     * @return 包含所有项目的列表
     * @throws IOException 网络请求失败时抛出
     */
    @GET("posts")
    suspend fun getItems(): List<Item>

    /**
     * 分页获取项目列表
     *
     * @param start 起始位置（从0开始）
     * @param limit 每页数量
     * @return 包含指定范围内项目的列表
     * @throws IOException 网络请求失败时抛出
     */
    @GET("posts")
    suspend fun getItemsByPage(
        @Query("_start") start: Int,
        @Query("_limit") limit: Int
    ): List<Item>

    /**
     * 添加新项目
     *
     * @param item 要添加的项目对象
     * @return 添加成功后返回的项目对象（包含生成的ID）
     * @throws IOException 网络请求失败时抛出
     */
    @POST("posts")
    suspend fun addItem(@Body item: Item): Item

    /**
     * 根据ID获取单个项目
     *
     * @param id 要获取的项目ID
     * @return 对应的项目对象
     * @throws IOException 网络请求失败时抛出
     * @throws NotFoundException 当指定ID的项目不存在时抛出
     */
    @GET("posts/{id}")
    suspend fun getItem(@Path("id") id: Int): Item
}

/**
 * API 服务单例
 */
object ApiService {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    /**
     * 创建日志拦截器，用于记录网络请求和响应的详细信息
     * 根据消息内容分类记录：
     * - 请求信息（以-->开头）
     * - 响应信息（以<--开头）
     * - 响应数据（以{或[开头）
     * - 其他调试信息
     */
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

    /**
     * 创建网络请求拦截器，用于记录请求URL和响应状态码
     *
     * @param chain 拦截器链，用于继续处理请求
     * @return 服务器响应
     */
    private val networkInterceptor = { chain: okhttp3.Interceptor.Chain ->
        val request = chain.request()
        Log.d(TAG, "请求URL: ${request.url}")
        val response = chain.proceed(request)
        Log.d(TAG, "响应码: ${response.code}")
        response
    }

    /**
     * 创建 OkHttpClient 实例，配置网络请求参数
     * 包含以下配置：
     * - 添加日志拦截器
     * - 添加网络请求拦截器
     * - 设置连接超时（15秒）
     * - 设置读取超时（15秒）
     * - 设置写入超时（15秒）
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor(networkInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    /**
     * 创建 Retrofit 实例，配置网络请求客户端和转换器
     * 包含以下配置：
     * - 设置基础URL
     * - 配置自定义的OkHttpClient
     * - 添加Gson转换器
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * API接口实例，用于执行所有网络请求
     *
     * 通过Retrofit.create()方法创建，提供对ApiInterface中定义的所有方法的访问
     */
    val api: ApiInterface = retrofit.create(ApiInterface::class.java)
} 