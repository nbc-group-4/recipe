package nbc.group.recipes.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import nbc.group.recipes.data.model.dto.Recipe
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
}
