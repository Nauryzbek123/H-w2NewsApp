package dev.androidbroadcast.news.data

import dev.androidbroadcast.news.data.model.Article
import dev.androidbroadcast.news.data.model.Source
import dev.androidbroadcast.news.database.models.ArticleDBO
import dev.androidbroadcast.newsapi.models.ArticleDTO
import dev.androidbroadcast.news.database.models.Source as SourceDBO
internal fun ArticleDBO.toArticle(): Article {
    return Article(
        cacheId = id,
        source = Source(id = source.id, name = source.name),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

internal fun ArticleDTO.toArticle(): Article {
    return Article(
        source = Source(id = source.id, name = source.name),
        author = author ?: "",
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

internal fun ArticleDTO.toArticleDbo(): ArticleDBO{
    return ArticleDBO(
        source = SourceDBO(id = source.id, name = source.name),
        author = author ?: "",
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
        id = 0
    )
}