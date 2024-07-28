package nbc.group.recipes.data.remote

import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeResponse
import nbc.group.recipes.data.network.RecipeService
import javax.inject.Inject
import javax.inject.Named


/**
 *
 * RecipeRepositoryImpl - RecipeDataSource
 *  완전히 겹침 -> source 삭제
 *
 * */
class RecipeDataSource @Inject constructor( // ->  Hilt(Dagger)가 해당 클래스의 인스턴스를 생성할 때 사용할 생성자임을 의미
    @Named("RecipeService") private val service: RecipeService
) {
    suspend fun getRecipe(
        startIndex: Int = 1,
        endIndex: Int = 20,
        recipeName: String,
        recipeId: Int,
    ) = service.getRecipe(
        startIndex = startIndex,
        endIndex = endIndex,
        recipeName = recipeName,
        recipeId = recipeId
    )

    suspend fun getRecipeIngredient(
        startIndex: Int = 1,
        endIndex: Int = 20,
        ingredientName: String,
        recipeId: Int,
    ) = service.getRecipeIngredient(
        startIndex = startIndex,
        endIndex = endIndex,
        ingredientName = ingredientName,
        recipeId = recipeId
    )

    suspend fun getRecipeIngredient(
        startIndex: Int,
        endIndex: Int,
        ingredientName: String
    ) = service.getRecipeIngredients(
        startIndex = startIndex,
        endIndex = endIndex,
        ingredientName = ingredientName,
    )

    suspend fun getRecipeIngredient(
        startIndex: Int,
        endIndex: Int,
        recipeId: Int
    ) = service.getRecipeIngredientsId(
        startIndex = startIndex,
        endIndex = endIndex,
        recipeId = recipeId,
    )

    suspend fun getRecipeV2(
        recipeId: Int
    ) = service.getRecipeV2(recipeId)

    suspend fun getRecipeIngredientV2(
        ingredientName: String
    ) = service.getRecipeIngredientV2(ingredientName)

    suspend fun getRecipeIngredientV3(
        startIndex: Int,
        endIndex: Int,
        ingredientName: String
    ) = service.getRecipeIngredientV3(
        startIndex,
        endIndex,
        ingredientName
    )

    suspend fun getRecipeProcedureV2(
        recipeId: Int
    ) = service.getRecipeProcedureV2(recipeId)

    suspend fun getRecipeProcedure(
        startIndex: Int = 1,
        endIndex: Int = 20,
        recipeId: Int,
    ) = service.getRecipeProcedure(
        startIndex = startIndex,
        endIndex = endIndex,
        recipeId = recipeId
    )
}