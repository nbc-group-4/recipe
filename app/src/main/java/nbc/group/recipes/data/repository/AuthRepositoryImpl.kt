package nbc.group.recipes.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import nbc.group.recipes.data.network.NetworkResult
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun signIn(id: String, pw: String): NetworkResult<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(id, pw).await()
            NetworkResult.Success(result.user!!)
        } catch(e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override suspend fun signUp(name: String, id: String, pw: String): NetworkResult<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(id, pw).await()
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            NetworkResult.Success(result.user!!)
        } catch(e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override suspend fun resign(): NetworkResult<Boolean> {
        return try {
            val target = firebaseAuth.currentUser!!
            val result = target.delete().await()
            NetworkResult.Success(true)
        } catch(e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}