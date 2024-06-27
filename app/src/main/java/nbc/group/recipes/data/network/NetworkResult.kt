package nbc.group.recipes.data.network

import java.lang.Exception

sealed class NetworkResult<out R> {
    data class Success<out R>(val result: R): NetworkResult<R>()
    data class Failure(val exception: Exception): NetworkResult<Nothing>()
    object Loading: NetworkResult<Nothing>()

}
