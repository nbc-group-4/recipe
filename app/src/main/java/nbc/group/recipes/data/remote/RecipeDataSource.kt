package nbc.group.recipes.data.remote

import nbc.group.recipes.data.network.RecipeService
import javax.inject.Inject
import javax.inject.Named

class RecipeDataSource @Inject constructor(
    @Named("RecipeService") private val service: RecipeService
) {
    suspend fun getRecipe(
//        startIndex: Int = 1,
//        endIndex: Int = 5,
//        recipeName: String,
//        recipeId: Int,
    ) = service.getRecipe(
//        startIndex = startIndex,
//        endIndex = endIndex,
//        recipeName = recipeName,
//        recipeId = recipeId
    )

    suspend fun getRecipeIngredient(
        startIndex: Int = 1,
        endIndex: Int = 5,
        ingredientName: String,
        recipeId: Int,
    ) = service.getRecipeIngredient(
        startIndex = startIndex,
        endIndex = endIndex,
        ingredientName = ingredientName,
        recipeId = recipeId
    )

    suspend fun getRecipeProcedure(
        startIndex: Int = 1,
        endIndex: Int = 5,
        recipeId: Int,
    ) = service.getRecipeProcedure(
        startIndex = startIndex,
        endIndex = endIndex,
        recipeId = recipeId
    )
}