package dev.androidbroadcast.news.data

import dev.androidbroadcast.news.data.model.Article
import dev.androidbroadcast.news.database.NewsDatabase
import dev.androidbroadcast.news.database.models.ArticleDBO
import dev.androidbroadcast.newsapi.NewsApi
import dev.androidbroadcast.newsapi.models.ArticleDTO
import dev.androidbroadcast.newsapi.models.ResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import java.io.IOException

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {
     fun getAll(): Flow<RequestResult<List<Article>>>{
         val cachedAllArticles: Flow<RequestResult<List<Article>?>> = getAllFromDatabase()
             .map { result ->
                 result.map { articleDbos ->
                 articleDbos?.map { it.toArticle() }
             }
             }

         var remoteArticles = getAllFromServer()


         return cachedAllArticles.combine(remoteArticles){dbos: RequestResult<Article>,dtos: RequestResult<Article> ->

         }
    }

    private fun getAllFromServer(): Flow<RequestResult<ResponseDTO<ArticleDTO>>> {
        val apiRequest = flow {emit(api.everything()) }
            .onEach { result ->
                if (result.isSuccess){
                    saveNetResponseToCache(checkNotNull(result.getOrThrow()).articles)
                }
            }
            .map { it.toRequestResult() }
        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())

        return merge<RequestResult<ResponseDTO<ArticleDTO>>>(apiRequest,start)
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDTO -> articleDTO.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

    private fun getAllFromDatabase(): Flow<RequestResult.Success<List<ArticleDBO>>> {
        return database.articlesDao
            .getAll()
            .map { RequestResult.Success(it) }
    }

    suspend fun search(query:String): Flow<Article>{
        api.everything()
        TODO("Not implemented")
    }
}



sealed class RequestResult<E>(internal val data:E? = null) {
    class  InProgress<E>( data:E? = null): RequestResult<E>(data)
    class  Success<E>( data:E): RequestResult<E>(data)
    class Error<E>: RequestResult<E>()
}

internal  fun <T: Any> RequestResult<T?>.requireData(): T = checkNotNull(data)

internal  fun <I,O> RequestResult<I>.map(mapper: (I?) -> O): RequestResult<O>{
    val outData = mapper(data)
    return when(this){
        is RequestResult.Success -> RequestResult.Success(outData)
        is RequestResult.Error -> RequestResult.Error()
        is RequestResult.InProgress -> RequestResult.InProgress(outData)
    }
}

internal fun <T> Result<T>.toRequestResult(): RequestResult<T>{
    return  when{
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error()
        else -> error("Impossible branch")
    }
}