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
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Url

interface NewsApi {

    @GET("/everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("languages") languages: List<Language>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pagesSize") @IntRange(from = 0, to = 180) pageSize: Int = 180,
        @Query("page") @IntRange(from = 1) page: Int = 1
        ): Response<Article>
}

    fun NewsApi(
        baseUrl: String,
        okHttpClient: OkHttpClient? = null
    ): NewsApi{
        val retrofit = retrofit(baseUrl,okHttpClient)

        return  retrofit.create(NewsApi::class.java)

    }

    private fun retrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient?
    ): Retrofit{
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .run { if(okHttpClient != null) client(okHttpClient) else this }
            .build()
        return retrofit
    }


