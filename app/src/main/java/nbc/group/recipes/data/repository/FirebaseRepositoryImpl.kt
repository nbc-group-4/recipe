package nbc.group.recipes.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
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
    private val storage: FirebaseStorage,
) : FirebaseRepository {
    override suspend fun putImage(
        storagePath: String,
        inputStream: InputStream,
    ): FirebaseResult<Boolean> {
        return try {
            val profileRef = storage.reference
                .child(storagePath)
            val result = profileRef.putStream(inputStream).await()
            FirebaseResult.Success(true)
        } catch (e: Exception) {
            FirebaseResult.Failure(e)
        }
    }

    override suspend fun getRecipes(): FirebaseResult<List<Recipe>> {
        return try {
            val result = firestore.collection("recipes").get().await()
            FirebaseResult.Success(result.toObjects(Recipe::class.java))
        } catch (e: Exception) {
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
        recipe: Recipe,
        imageStreamList: List<InputStream>,
    ): FirebaseResult<Boolean> {
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
                recipeRef.putStream(imageStreamList[i]).await()
            }
            FirebaseResult.Success(true)
        } catch (e: Exception) {
            FirebaseResult.Failure(e)
        }
    }
}