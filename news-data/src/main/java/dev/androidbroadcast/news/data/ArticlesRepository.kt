package dev.androidbroadcast.news.data

import dev.androidbroadcast.news.data.model.Article
import dev.androidbroadcast.news.database.NewsDatabase
import dev.androidbroadcast.newsapi.NewsApi
import dev.androidbroadcast.newsapi.models.ArticleDTO
import dev.androidbroadcast.newsapi.models.ResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.io.IOException

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {
     fun getAll(): Flow<RequestResult<List<Article>>>{
         val cachedAllArticles: Flow<List<Article>> = database.articlesDao
             .getAll()
             .map { articles -> articles.map { it.toArticle() } }

         var remoteArticles = flow {
             emit(api.everything())
         }.map {result ->
             if (result.isSuccess){
                 val response: ResponseDTO<ArticleDTO> = result.getOrThrow()
                 response.articles
             }else{
                 throw result.exceptionOrNull() ?: IOException()
             }
         }.map { articlesDtos ->
             articlesDtos.map { articleDTO -> articleDTO.toArticleDbo() }
         }.onEach { articlesDbos ->
             database.articlesDao.insert(articlesDbos)
         }

        cachedAllArticles.map {

        }
         return cachedAllArticles.combine(remoteArticles)
    }

    suspend fun search(query:String): Flow<Article>{
        api.everything()
        TODO("Not implemented")
    }
}



sealed class RequestResult<E>(protected val data:E?) {
    class  InProgress<E>( data:E?): RequestResult<E>(data)
    class  Success<E>( data:E?): RequestResult<E>(data)
    class Error<E>( data:E?): RequestResult<E>(data)
}