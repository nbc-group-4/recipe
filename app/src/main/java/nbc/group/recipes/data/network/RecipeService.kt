package nbc.group.recipes.data.network

import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeProcedure
import nbc.group.recipes.data.model.dto.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeService {

    @GET(RECIPE_API)
    suspend fun getRecipe(
        @Query("API_KEY") apiKey: String = RECIPE_API_KEY,
        @Query("TYPE") type: String = "json",
        @Query("API_URL") apiUrl: String = RECIPE_API,
        @Query("START_INDEX") startIndex: Int,
        @Query("END_INDEX") endIndex: Int,
        @Query("RECIPE_NM_KO") recipeName: String,
        @Query("RECIPE_ID") recipeId: Int,
    ): RecipeResponse<Recipe>

    // TODO: URL 확인 필요
    @GET(RECIPE_INGREDIENT_API)
    suspend fun getRecipeIngredient(
        @Query("API_KEY") apiKey: String = RECIPE_API_KEY,
        @Query("TYPE") type: String = "json",
        @Query("API_URL") apiUrl: String = RECIPE_INGREDIENT_API,
        @Query("START_INDEX") startIndex: Int,
        @Query("END_INDEX") endIndex: Int,
        @Query("IRDNT_NM") ingredientName: String,
        @Query("RECIPE_ID") recipeId: Int,
    ): RecipeResponse<RecipeIngredient>

    @GET(RECIPE_PROCEDURE_API)
    suspend fun getRecipeProcedure(
        @Query("API_KEY") apiKey: String = RECIPE_API_KEY,
        @Query("TYPE") type: String = "json",
        @Query("API_URL") apiUrl: String = RECIPE_PROCEDURE_API,
        @Query("START_INDEX") startIndex: Int,
        @Query("END_INDEX") endIndex: Int,
        @Query("RECIPE_ID") recipeId: Int,
    ): RecipeResponse<RecipeProcedure>

}

