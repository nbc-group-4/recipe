package nbc.group.recipes.data.repository

import com.google.firebase.auth.FirebaseUser
import nbc.group.recipes.data.network.FirebaseResult

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun signIn(id: String, pw: String): FirebaseResult<FirebaseUser>
    suspend fun signUp(name: String, id: String, pw: String): FirebaseResult<FirebaseUser>
    fun logout()
}
