package nbc.group.recipes.data.repository

import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeProcedure
import nbc.group.recipes.data.model.dto.RecipeResponse
import nbc.group.recipes.data.network.RecipeService
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeService: RecipeService
) : RecipeRepository {
    override suspend fun getRecipes(
        startIndex: Int,
        endIndex: Int,
        recipeName: String,
        recipeId: Int
    ): RecipeResponse<Recipe> {
        return recipeService.getRecipe(
            startIndex = startIndex,
            endIndex = endIndex,
            recipeName = recipeName,
            recipeId = recipeId
        )
    }

    override suspend fun getRecipeIngredients(
        startIndex: Int,
        endIndex: Int,
        ingredientName: String,
        recipeId: Int
    ): RecipeResponse<RecipeIngredient> {
        return recipeService.getRecipeIngredient(
            startIndex = startIndex,
            endIndex = endIndex,
            ingredientName = ingredientName,
            recipeId = recipeId
        )
    }

    override suspend fun getRecipeProcedures(
        startIndex: Int,
        endIndex: Int,
        recipeId: Int
    ): RecipeResponse<RecipeProcedure> {
        return recipeService.getRecipeProcedure(
            startIndex = startIndex,
            endIndex = endIndex,
            recipeId = recipeId
        )
    }
}