package nbc.group.recipes.data.repository

import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.network.FirebaseResult

interface FirestoreRepository {
    suspend fun getRecipes(): FirebaseResult<List<Recipe>>
    suspend fun putRecipe(recipe: Recipe): FirebaseResult<Boolean>
}
