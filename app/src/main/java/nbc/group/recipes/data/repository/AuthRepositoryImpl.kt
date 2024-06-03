package nbc.group.recipes.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import nbc.group.recipes.data.network.FirebaseResult
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun signIn(id: String, pw: String): FirebaseResult<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(id, pw).await()
            FirebaseResult.Success(result.user!!)
        } catch(e: Exception) {
            FirebaseResult.Failure(e)
        }
    }

    override suspend fun signUp(name: String, id: String, pw: String): FirebaseResult<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(id, pw).await()
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            FirebaseResult.Success(result.user!!)
        } catch(e: Exception) {
            FirebaseResult.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}