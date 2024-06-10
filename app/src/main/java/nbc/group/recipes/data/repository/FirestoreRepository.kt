package nbc.group.recipes.data.repository

import com.google.firebase.firestore.DocumentReference
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.firebase.UserMetaData
import nbc.group.recipes.data.network.FirebaseResult

interface FirestoreRepository {
    suspend fun getRecipes(): FirebaseResult<List<Recipe>>
    suspend fun putRecipe(recipe: Recipe): FirebaseResult<Boolean>

    suspend fun getUserMeta(uid: String): FirebaseResult<UserMetaData>

    suspend fun putUserMeta(uid: String, userMetaData: UserMetaData): FirebaseResult<Boolean>

    suspend fun putRecipeTransaction(uid: String, recipe: Recipe): FirebaseResult<Boolean>
}
