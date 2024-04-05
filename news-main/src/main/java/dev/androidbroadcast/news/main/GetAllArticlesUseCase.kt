package dev.androidbroadcast.news.main

import dev.androidbroadcast.news.data.ArticlesRepository
import dev.androidbroadcast.news.data.RequestResult
import dev.androidbroadcast.news.data.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import dev.androidbroadcast.news.data.model.Article as DataArticle

class GetAllArticlesUseCase @Inject constructor(private  val repository: ArticlesRepository ) {

    operator fun invoke(): Flow<RequestResult<List<DataArticle>>> {

       return repository.getAll()
            .map { requestResult ->
                requestResult.map {articles ->
                    articles.map { it.toUiArticles() }
                } }
    }

}
private fun DataArticle.toUiArticles(): DataArticle {
    TODO("Not yet implemented")
}