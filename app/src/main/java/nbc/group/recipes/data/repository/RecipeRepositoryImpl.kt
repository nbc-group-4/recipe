package nbc.group.recipes.data.repository

import nbc.group.recipes.data.local.dao.RecipeDao
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeIngredientWrapper
import nbc.group.recipes.data.model.dto.RecipeProcedure
import nbc.group.recipes.data.model.dto.RecipeProcedureWrapper
import nbc.group.recipes.data.model.dto.RecipeResponse
import nbc.group.recipes.data.model.dto.RecipeWrapper
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.network.RecipeService
import nbc.group.recipes.data.remote.RecipeDataSource
import javax.inject.Inject
import javax.inject.Named

class RecipeRepositoryImpl @Inject constructor(
    private val recipeDataSource: RecipeDataSource,
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override suspend fun putRecipeEntity(
        recipeEntity: RecipeEntity
    ) = recipeDao.insertData(recipeEntity)

    override suspend fun getRecipes(
        startIndex: Int,
        endIndex: Int,
        recipeName: String,
        recipeId: Int
    ): RecipeResponse<Recipe> {
        return recipeDataSource.getRecipe(
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
        return recipeDataSource.getRecipeIngredient(
            startIndex = startIndex,
            endIndex = endIndex,
            ingredientName = ingredientName,
            recipeId = recipeId
        )
    }

    override suspend fun getRecipeIngredients(
        startIndex: Int,
        endIndex: Int,
        ingredientName: String,
    ): RecipeResponse<RecipeIngredient> {
        return recipeDataSource.getRecipeIngredient(
            startIndex = startIndex,
            endIndex = endIndex,
            ingredientName = ingredientName,
        )
    }

    override suspend fun getRecipeIngredients(
        startIndex: Int,
        endIndex: Int,
        recipeId: Int
    ): RecipeResponse<RecipeIngredient> {
        return recipeDataSource.getRecipeIngredient(
            startIndex = startIndex,
            endIndex = endIndex,
            recipeId = recipeId,
        )
    }

    override suspend fun getRecipeIngredientsV2(
        ingredientName: String
    ) = recipeDataSource.getRecipeIngredientV2(ingredientName)

    override suspend fun getRecipesV2(
        recipeId: Int
    ) = recipeDataSource.getRecipeV2(recipeId)

    override suspend fun getRecipeProceduresV2(
        recipeId: Int
    ) = recipeDataSource.getRecipeProcedureV2(recipeId)

    override suspend fun getRecipeProcedures(
        startIndex: Int,
        endIndex: Int,
        recipeId: Int
    ): RecipeResponse<RecipeProcedure> {
        return recipeDataSource.getRecipeProcedure(
            startIndex = startIndex,
            endIndex = endIndex,
            recipeId = recipeId
        )
    }
}