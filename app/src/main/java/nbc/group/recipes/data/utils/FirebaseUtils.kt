package nbc.group.recipes.data.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

//suspend fun <T> Task<T>.await(): T {
//    return suspendCancellableCoroutine { cont ->
//        addOnCompleteListener {
//            if(it.exception != null) {
//                cont.resumeWithException(it.exception!!)
//            } else {
//                cont.resume(it.result, null)
//            }
//        }
//    }
//}

fun getUserProfileStoragePath(uid: String): String {
    return "userProfile/$uid/profile.jpg"
}

fun getUserMetaFirestorePath(uid: String): String {
    return ""
}

fun getRecipeStoragePath(recipeId: String): String {
    return "recipeImage/$recipeId/0.jpg"
}
