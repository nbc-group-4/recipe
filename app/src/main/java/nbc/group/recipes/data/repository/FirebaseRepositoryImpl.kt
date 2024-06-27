package nbc.group.recipes.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.firebase.UserMetaData
import nbc.group.recipes.data.network.NetworkResult
import java.io.InputStream
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : FirebaseRepository {
    override suspend fun putImage(
        storagePath: String,
        inputStream: InputStream,
    ): NetworkResult<Boolean> {
        return try {
            val profileRef = storage.reference
                .child(storagePath)
            val result = profileRef.putStream(inputStream).await()
            NetworkResult.Success(true)
        } catch (e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override suspend fun getRecipes(): NetworkResult<List<Recipe>> {
        return try {
            val result = firestore.collection("recipes").get().await()
            NetworkResult.Success(result.toObjects(Recipe::class.java))
        } catch (e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override suspend fun getRecipesByIngredient(ingredient: String): NetworkResult<List<Recipe>> {
        return try {
            val result = firestore
                .collection("recipes")
                .whereEqualTo("ingredientCode", ingredient)
                .get().await()
            NetworkResult.Success(result.toObjects(Recipe::class.java))
        } catch (e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override suspend fun getRecipeForTest(
        ingredient: String
    ): NetworkResult<List<Recipe>> {
        return try {
            val result = firestore.collection("recipes").get().await()
            val temp = mutableListOf<Recipe>()
            result.documents.forEach {
                it.data?.let { map ->
                    temp.add(
                        Recipe(
                            recipeName = "${it.id}/${map["recipeName"] as String}",
                            summary = map["summary"] as String,
                            nationCode = map["nationCode"] as String,
                            nationName = map["nationName"] as String,
                            cookingTime = map["cookingTime"] as String,
                            typeCode = map["typeCode"] as String,
                            typeName = map["typeName"] as String,
                            levelName = map["levelName"] as String,
                            ingredientCode = map["ingredientCode"] as String,
                        )
                    )
                }
            }
            NetworkResult.Success(temp)
        } catch (e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override suspend fun putRecipe(recipe: Recipe): NetworkResult<Boolean> {
        return try {
            val result = firestore.collection("recipes").add(recipe).await()
            NetworkResult.Success(true)
        } catch (e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override suspend fun getUserMeta(uid: String): NetworkResult<UserMetaData> {
        return try {
            val result = firestore.collection("userMeta").document(uid).get().await()
            NetworkResult.Success(result.toObject<UserMetaData>() ?: UserMetaData())
        } catch (e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override suspend fun putUserMeta(
        uid: String,
        userMetaData: UserMetaData,
    ): NetworkResult<Boolean> {
        return try {
            val result = firestore.collection("userMeta/$uid").add(userMetaData).await()
            val temp = firestore.collection("userMeta")
            NetworkResult.Success(true)
        } catch (e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override suspend fun putRecipeTransaction(
        uid: String,
        recipe: Recipe,
        imageStreamList: List<InputStream>,
    ): NetworkResult<Boolean> {
        return try {
            val result1 = firestore.collection("recipes").add(recipe).await()
            val result2 = firestore.collection("userMeta").document(uid).get().await()
            val meta = result2.toObject<UserMetaData>() ?: UserMetaData()
            val temp = meta.recipeIds.toMutableList()
            temp.add(result1.id)
            val result3 = firestore.collection("userMeta")
                .document(uid).set(meta.copy(temp)).await()
            for(i in imageStreamList.indices) {
                val recipeRef = storage.reference.child("recipeImage/${result1.id}/$i.jpg")
                val result4 = recipeRef.putStream(imageStreamList[i]).await()
            }
            NetworkResult.Success(true)
        } catch (e: Exception) {
            NetworkResult.Failure(e)
        }
    }
}