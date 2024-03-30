package dev.androidbroadcast.news.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androidbroadcast.news.data.ArticlesRepository
import dev.androidbroadcast.news.data.RequestResult
import dev.androidbroadcast.news.data.map
import dev.androidbroadcast.news.data.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

internal class NewsMainViewModel @Inject constructor(
     getAllArticlesUseCase: Provider<GetAllArticlesUseCase>,
): ViewModel() {

    val state: StateFlow<State> =  getAllArticlesUseCase.get().invoke()
        .map { it.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily,State.None)


}

private fun  RequestResult<List<Article>>.toState(): State {
    return when(this){
        is RequestResult.Error -> State.Error()
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(checkNotNull(data))
    }
}

sealed class State{
    object None: State()
    class  Loading(val articles: List<Article>? = null): State()
    class Error(val articles: List<Article>? = null): State()
    class Success(val articles: List<Article>) : State()
}