package nbc.group.recipes.data.repository

import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.model.firebase.UserMetaData
import nbc.group.recipes.data.network.NetworkResult
import java.io.InputStream

interface FirebaseRepository {

    suspend fun putImage(
        storagePath: String,
        inputStream: InputStream
    ): NetworkResult<Boolean>

    suspend fun getRecipes(): NetworkResult<List<Recipe>>
    suspend fun getRecipesByIngredient(ingredient: String): NetworkResult<List<Recipe>>
    suspend fun putRecipe(recipe: Recipe): NetworkResult<Boolean>
    suspend fun getUserMeta(uid: String): NetworkResult<UserMetaData>
    suspend fun putUserMeta(uid: String, userMetaData: UserMetaData): NetworkResult<Boolean>
    suspend fun putRecipeTransaction(uid: String, recipe: Recipe, imageStreamList: List<InputStream>): NetworkResult<Boolean>

    suspend fun getRecipeForTest(ingredient: String): NetworkResult<List<Recipe>>
    suspend fun getRecipeForTestV2(ingredient: String): NetworkResult<List<RecipeEntity>>
}