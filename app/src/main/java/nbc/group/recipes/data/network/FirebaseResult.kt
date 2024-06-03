package nbc.group.recipes.data.network

import java.lang.Exception

sealed class FirebaseResult<out R> {
    data class Success<out R>(val result: R): FirebaseResult<R>()
    data class Failure(val exception: Exception): FirebaseResult<Nothing>()
    object Loading: FirebaseResult<Nothing>()

}
