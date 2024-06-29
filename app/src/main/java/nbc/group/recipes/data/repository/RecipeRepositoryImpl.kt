package nbc.group.recipes.data.repository

import com.google.gson.Gson
import nbc.group.recipes.data.local.dao.RecipeDao
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeIngredientWrapper
import nbc.group.recipes.data.model.dto.RecipeProcedure
import nbc.group.recipes.data.model.dto.RecipeProcedureWrapper
import nbc.group.recipes.data.model.dto.RecipeResponse
import nbc.group.recipes.data.model.dto.RecipeWrapper
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.network.NetworkResult
import nbc.group.recipes.data.network.RecipeService
import nbc.group.recipes.data.remote.RecipeDataSource
import nbc.group.recipes.getRecipeImageUrl
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


    override suspend fun getRecipeIngredientsV3(
        ingredientName: String
    ): NetworkResult<List<RecipeIngredient>> {
        return try {
            val temp = recipeDataSource.getRecipeIngredientV2(ingredientName)
            NetworkResult.Success(temp.recipeIngredient.row)
        } catch(e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override suspend fun getRecipeIngredientsV4(
        startIndex: Int,
        endIndex: Int,
        ingredientName: String
    ): NetworkResult<List<RecipeIngredient>> {
        return try {
            val temp = recipeDataSource.getRecipeIngredientV3(startIndex, endIndex, ingredientName)
            NetworkResult.Success(temp.recipeIngredient.row)
        } catch(e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override suspend fun getRecipesV3(
        recipeId: Int
    ): NetworkResult<List<Recipe>> {
        return try {
            val temp = recipeDataSource.getRecipeV2(recipeId)
            NetworkResult.Success(temp.recipe.row)
        } catch(e: Exception) {
            NetworkResult.Failure(e)
        }
    }

    override suspend fun getRecipesV4(
        startIndex: Int,
        endIndex: Int,
        ingredientName: String
    ): NetworkResult<List<RecipeEntity>> {
        val temp = getRecipeIngredientsV4(startIndex, endIndex, ingredientName)
        return when(temp) {
            is NetworkResult.Success -> {
                val temp2 = mutableListOf<RecipeEntity>()
                temp.result.forEach {

                    try {
                        val temp3 = recipeDataSource.getRecipeV2(it.recipeId)
                        // val temp4 = recipeDataSource.getRecipeProcedureV2(it.recipeId)
                        val recipe = temp3.recipe.row[0]
                        val temp4 = getRecipeImageUrl(recipe.recipeName) ?: ""

                        temp2.add(
                            RecipeEntity(
                                id = recipe.recipeId,
                                recipeImg = temp4,
                                recipeName = recipe.recipeName,
                                explain = recipe.summary,
                                step = "", // api를 복합적으로 호출하여 너무 무거워 해당 정보가 필요한 곳에서 호출하여 정보를 받는 것이 좋겠다.
                                ingredient = ingredientName,
                                difficulty = recipe.levelName,
                                time = recipe.cookingTime,
                            )
                        )
                    } catch(e: Exception) {

                    }
                }
                NetworkResult.Success(temp2)
            }
            is NetworkResult.Loading -> {
                NetworkResult.Loading
            }
            is NetworkResult.Failure -> {
                NetworkResult.Failure(temp.exception)
            }
        }
    }
}