package nbc.group.recipes.data.repository

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.firebase.UserMetaData
import nbc.group.recipes.data.network.FirebaseResult
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class FirestoreRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): FirestoreRepository {

    override suspend fun getRecipes(): FirebaseResult<List<Recipe>> {
        return try {
            val result = db.collection("recipes").get().await()
            FirebaseResult.Success(result.toObjects(Recipe::class.java))
        } catch(e: Exception) {
            FirebaseResult.Failure(e)
        }
    }

    override suspend fun putRecipe(recipe: Recipe): FirebaseResult<Boolean> {
        return try {
            val result = db.collection("recipes").add(recipe).await()
            FirebaseResult.Success(true)
        } catch (e: Exception) {
            FirebaseResult.Failure(e)
        }
    }

    override suspend fun getUserMeta(uid: String): FirebaseResult<UserMetaData> {
        return try {
            val result = db.collection("userMeta").document(uid).get().await()
            FirebaseResult.Success(UserMetaData(recipeIds = result.get("recipeIds") as List<String>))
        } catch (e: Exception) {
            FirebaseResult.Failure(e)
        }
    }

    override suspend fun putUserMeta(
        uid: String,
        userMetaData: UserMetaData,
    ): FirebaseResult<Boolean> {
        return try {
            val result = db.collection("userMeta")
                .document(uid).set(userMetaData).await()
            // val result = db.collection("userMeta/${uid}").add(userMetaData).await()
            FirebaseResult.Success(true)
        } catch (e: Exception) {
            FirebaseResult.Failure(e)
        }
    }

    override suspend fun putRecipeTransaction(uid: String, recipe: Recipe): FirebaseResult<Boolean> {
        return try {
            val result1 = db.collection("recipes").add(recipe).await()
            val result2 = db.collection("userMeta").document(uid).get().await()
            val meta = result2.toObject<UserMetaData>() ?: UserMetaData()
            val temp = meta.recipeIds.toMutableList()
            temp.add(result1.id)
            val result3 = db.collection("userMeta")
                .document(uid).set(meta.copy(temp)).await()
            FirebaseResult.Success(true)
        } catch (e: Exception) {
            FirebaseResult.Failure(e)
        }
    }
}
