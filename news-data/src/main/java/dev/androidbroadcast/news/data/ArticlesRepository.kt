package dev.androidbroadcast.news.data

import dev.androidbroadcast.news.data.model.Article
import dev.androidbroadcast.news.database.NewsDatabase
import dev.androidbroadcast.newsapi.NewsApi
import kotlinx.coroutines.flow.Flow

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {
    suspend fun getAll(): Flow<Article>{
        api.everything()
        TODO("Not implemented")
    }

    suspend fun search(query:String): Flow<Article>{
        api.everything()
        TODO("Not implemented")
    }
}