package dev.androidbroadcast.newsapi

import kotlinx.serialization.SerialName
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

interface NewsApi {

    @GET("/everything")
    fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("to") languages: String? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pagesSize") pageSize: Int = 180,
        @Query("page") page: Int = 1
        )
}

enum class SortBy{

    @SerialName("relevancy")
    RELEVANCY,

    @SerialName("popularity")
    POPULARITY,

    @SerialName("publishedAt")
    PUBLISHED_AT
}