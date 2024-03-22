package dev.androidbroadcast.newsapi

import kotlinx.serialization.SerialName
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date
import androidx.annotation.IntRange
import dev.androidbroadcast.newsapi.models.Article
import dev.androidbroadcast.newsapi.models.Language
import dev.androidbroadcast.newsapi.models.Response
import dev.androidbroadcast.newsapi.models.SortBy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import dev.androidbroadcast.newsapi.utils.TimeApiKeyInterceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Header
import retrofit2.http.Url

interface NewsApi {

    @GET("/everything")
    suspend fun everything(
        @Header("X-Api-Key") apiKey: String,
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("languages") languages: List<Language>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pagesSize") @IntRange(from = 0, to = 180) pageSize: Int = 180,
        @Query("page") @IntRange(from = 1) page: Int = 1
        ): Result<Response<Article>>
}

    fun NewsApi(
        baseUrl: String,
        apiKey: String,
        okHttpClient: OkHttpClient? = null,
        json: Json = Json,
    ): NewsApi{

        return  retrofit(baseUrl,apiKey,okHttpClient,json).create()

    }

    private fun retrofit(
        baseUrl: String,
        apiKey: String,
        okHttpClient: OkHttpClient? = null ,
        json: Json = Json
    ): Retrofit{
        val jsonConverterFactory = json.asConverterFactory(MediaType.get("application/json"))

        val modifiedOkHttpClient: OkHttpClient = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
             .addInterceptor(TimeApiKeyInterceptor(apiKey))
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(jsonConverterFactory)
            .addCallAdapterFactory(ResultCallAdapterFactory.create())
            .client(modifiedOkHttpClient)
            .build()
    }


