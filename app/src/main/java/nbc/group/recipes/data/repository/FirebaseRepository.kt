package nbc.group.recipes.data.repository

import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.firebase.UserMetaData
import nbc.group.recipes.data.network.FirebaseResult
import java.io.InputStream

interface FirebaseRepository {

    suspend fun putImage(
        storagePath: String,
        inputStream: InputStream
    ): FirebaseResult<Boolean>

    suspend fun getRecipes(): FirebaseResult<List<Recipe>>
    suspend fun getRecipesByIngredient(ingredient: String): FirebaseResult<List<Recipe>>
    suspend fun putRecipe(recipe: Recipe): FirebaseResult<Boolean>
    suspend fun getUserMeta(uid: String): FirebaseResult<UserMetaData>
    suspend fun putUserMeta(uid: String, userMetaData: UserMetaData): FirebaseResult<Boolean>
    suspend fun putRecipeTransaction(uid: String, recipe: Recipe, imageStreamList: List<InputStream>): FirebaseResult<Boolean>

    suspend fun getRecipeForTest(ingredient: String): FirebaseResult<List<Recipe>>
}