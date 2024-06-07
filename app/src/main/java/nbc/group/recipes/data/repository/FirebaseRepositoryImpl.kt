package nbc.group.recipes.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.firebase.UserMetaData
import nbc.group.recipes.data.network.FirebaseResult
import java.io.InputStream
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
): FirebaseRepository {
    override suspend fun putImage(
        storagePath: String,
        inputStream: InputStream,
    ): FirebaseResult<Boolean> {
        return try {
            val profileRef = storage.reference
                .child(storagePath)
            val result = profileRef.putStream(inputStream).await()
            FirebaseResult.Success(true)
        } catch(e: Exception) {
            FirebaseResult.Failure(e)
        }
    }

    override suspend fun getRecipes(): FirebaseResult<List<Recipe>> {
        return try {
            val result = firestore.collection("recipes").get().await()
            FirebaseResult.Success(result.toObjects(Recipe::class.java))
        } catch(e: Exception) {
            FirebaseResult.Failure(e)
        }
    }

    override suspend fun putRecipe(recipe: Recipe): FirebaseResult<Boolean> {
        return try {
            val result = firestore.collection("recipes").add(recipe).await()
            FirebaseResult.Success(true)
        } catch (e: Exception) {
            FirebaseResult.Failure(e)
        }
    }

    override suspend fun getUserMeta(uid: String): FirebaseResult<UserMetaData> {
        return try {
            val result = firestore.collection("userMeta/${uid}").get().await()
            FirebaseResult.Success(result.toObjects(UserMetaData::class.java)[0])
        } catch (e: Exception) {
            FirebaseResult.Failure(e)
        }
    }

    override suspend fun putUserMeta(
        uid: String,
        userMetaData: UserMetaData,
    ): FirebaseResult<Boolean> {
        return try {
            val result = firestore.collection("userMeta/$uid").add(userMetaData).await()
            val temp = firestore.collection("userMeta")
            FirebaseResult.Success(true)
        } catch (e: Exception) {
            FirebaseResult.Failure(e)
        }
    }

    override suspend fun putRecipeTransaction(
        uid: String,
        recipe: Recipe
    ): FirebaseResult<Boolean> {
        val recipeRef = firestore.collection("recipes")
        val userMetaRef = firestore.collection("userMeta/$uid")
        return try {
            firestore.runTransaction { transaction ->
                val snapshot = firestore.collection("recipes")

                // todo: 0605 마지막 작업 중 -> firestore + storage 이를 통하여 transaction 구현
            }
            FirebaseResult.Success(true)
        } catch (e: Exception) {
            FirebaseResult.Failure(e)
        }
    }
}