package nbc.group.recipes.data.repository

import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeIngredientWrapper
import nbc.group.recipes.data.model.dto.RecipeProcedure
import nbc.group.recipes.data.model.dto.RecipeProcedureWrapper
import nbc.group.recipes.data.model.dto.RecipeResponse
import nbc.group.recipes.data.model.dto.RecipeWrapper
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.network.NetworkResult

interface RecipeRepository {
    suspend fun getRecipes(startIndex: Int, endIndex: Int, recipeName: String, recipeId: Int): RecipeResponse<Recipe>
    suspend fun getRecipesV2(recipeId: Int): RecipeWrapper
    suspend fun getRecipesV3(startIndex: Int, endIndex: Int, recipeId: Int): NetworkResult<List<Recipe>>
    suspend fun getRecipesV4(startIndex: Int, endIndex: Int, ingredientName: String): NetworkResult<List<RecipeEntity>>
    suspend fun getRecipeIngredients(startIndex: Int, endIndex: Int, ingredientName: String, recipeId: Int): RecipeResponse<RecipeIngredient>
    suspend fun getRecipeIngredients(startIndex: Int, endIndex: Int, ingredientName: String): RecipeResponse<RecipeIngredient>
    suspend fun getRecipeIngredients(startIndex: Int, endIndex: Int, recipeId: Int): RecipeResponse<RecipeIngredient>
    suspend fun getRecipeIngredientsV2(ingredientName: String): RecipeIngredientWrapper
    suspend fun getRecipeIngredientsV3(ingredientName: String): NetworkResult<List<RecipeIngredient>>
    suspend fun getRecipeIngredientsV4(startIndex: Int, endIndex: Int, ingredientName: String): NetworkResult<List<RecipeIngredient>>
    suspend fun getRecipeProcedures(startIndex: Int, endIndex: Int, recipeId: Int): RecipeResponse<RecipeProcedure>
    suspend fun getRecipeProceduresV2(recipeId: Int): RecipeProcedureWrapper
    suspend fun putRecipeEntity(recipeEntity: RecipeEntity) : Long


}