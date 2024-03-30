package dev.androidbroadcast.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class AppDispatchers(
    val default: CoroutineDispatcher = Dispatchers.Default,
    val io: CoroutineDispatcher = Dispatchers.IO,
    val main: CoroutineDispatcher = Dispatchers.Main,
    val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
) {

    companion object: AppDispatchers()
}