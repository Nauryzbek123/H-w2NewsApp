package dev.androidbroadcast.newsapi

import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date
import androidx.annotation.IntRange
import dev.androidbroadcast.newsapi.models.ArticleDTO
import dev.androidbroadcast.newsapi.models.LanguageDTO
import dev.androidbroadcast.newsapi.models.ResponseDTO
import dev.androidbroadcast.newsapi.models.SortBy
import kotlinx.serialization.json.Json
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import dev.androidbroadcast.newsapi.utils.NewsApiKeyInterceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create

interface NewsApi {
    @GET("everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("languages") languages: List< @JvmSuppressWildcards LanguageDTO>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pageSize") @IntRange(from =  0, to = 100) pageSize: Int = 100,
        @Query("page") @IntRange(from = 1) page: Int = 1

    ): Result<ResponseDTO<ArticleDTO>>

}


fun NewsApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json
): NewsApi{

    return retrofit(baseUrl,apiKey,okHttpClient,json).create()
}

private val json1 = Json {
    coerceInputValues = true
    ignoreUnknownKeys = true
    isLenient = true
}

@Suppress("SuspiciousIndentation")
private fun retrofit(baseUrl: String,
                     apiKey: String,
                     okHttpClient: OkHttpClient? = null,
                     json: Json,
) : Retrofit {



    val jsonConverterFactory = json1.asConverterFactory("application/json".toMediaType())

    val modifiedOkHttpClient: OkHttpClient =  (okHttpClient?.newBuilder() ?: OkHttpClient.Builder()).
    addInterceptor(NewsApiKeyInterceptor(apiKey)).build()


    return Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(modifiedOkHttpClient).
        build()
}

