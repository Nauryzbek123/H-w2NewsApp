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

interface NewsApi {

    @GET("/everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("to") languages: List<Language>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pagesSize") @IntRange(from = 0, to = 180) pageSize: Int = 180,
        @Query("page") @IntRange(from = 1) page: Int = 1
        ): Response<Article>
}


