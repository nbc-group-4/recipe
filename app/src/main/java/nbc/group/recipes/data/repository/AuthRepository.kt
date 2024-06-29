package nbc.group.recipes.data.repository

import com.google.firebase.auth.FirebaseUser
import nbc.group.recipes.data.network.NetworkResult

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun signIn(id: String, pw: String): NetworkResult<FirebaseUser>
    suspend fun signUp(name: String, id: String, pw: String): NetworkResult<FirebaseUser>
    suspend fun resign(): NetworkResult<Boolean>
    fun logout()
}
