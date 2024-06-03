package nbc.group.recipes.data.repository

import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeProcedure
import nbc.group.recipes.data.model.dto.RecipeResponse

interface RecipeRepository {
    suspend fun getRecipes(startIndex: Int, endIndex: Int, recipeName: String, recipeId: Int): RecipeResponse<Recipe>
    suspend fun getRecipeIngredients(startIndex: Int, endIndex: Int, ingredientName: String, recipeId: Int): RecipeResponse<RecipeIngredient>
    suspend fun getRecipeProcedures(startIndex: Int, endIndex: Int, recipeId: Int): RecipeResponse<RecipeProcedure>
}