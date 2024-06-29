package nbc.group.recipes.data.network

import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeIngredientWrapper
import nbc.group.recipes.data.model.dto.RecipeProcedure
import nbc.group.recipes.data.model.dto.RecipeProcedureWrapper
import nbc.group.recipes.data.model.dto.RecipeResponse
import nbc.group.recipes.data.model.dto.RecipeWrapper
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("Grid_20150827000000000226_1/{START_INDEX}/{END_INDEX}")
    suspend fun getRecipeV3(
        @Path("START_INDEX") startIndex: Int,
        @Path("END_INDEX") endIndex: Int,
        @Query("RECIPE_ID") recipeId: Int,
    ): RecipeWrapper

    @GET(RECIPE_API)
    suspend fun getRecipeV2(
        @Query("RECIPE_ID") recipeId: Int,
    ): RecipeWrapper

    @GET(RECIPE_INGREDIENT_API)
    suspend fun getRecipeIngredientV2(
        @Query("IRDNT_NM") ingredientName: String,
    ): RecipeIngredientWrapper

    @GET("Grid_20150827000000000227_1/{START_INDEX}/{END_INDEX}")
    suspend fun getRecipeIngredientV3(
        @Path("START_INDEX") startIndex: Int,
        @Path("END_INDEX") endIndex: Int,
        @Query("IRDNT_NM") ingredientName: String,
    ): RecipeIngredientWrapper

    @GET(RECIPE_PROCEDURE_API)
    suspend fun getRecipeProcedureV2(
        @Query("RECIPE_ID") recipeId: Int,
    ): RecipeProcedureWrapper


    @GET(RECIPE_INGREDIENT_API)
    suspend fun getRecipeIngredients(
        @Query("API_KEY") apiKey: String = RECIPE_API_KEY,
        @Query("TYPE") type: String = "json",
        @Query("API_URL") apiUrl: String = RECIPE_INGREDIENT_API,
        @Query("START_INDEX") startIndex: Int,
        @Query("END_INDEX") endIndex: Int,
        @Query("IRDNT_NM") ingredientName: String,
    ): RecipeResponse<RecipeIngredient>

    @GET(RECIPE_INGREDIENT_API)
    suspend fun getRecipeIngredientsId(
        @Query("API_KEY") apiKey: String = RECIPE_API_KEY,
        @Query("TYPE") type: String = "json",
        @Query("API_URL") apiUrl: String = RECIPE_INGREDIENT_API,
        @Query("START_INDEX") startIndex: Int,
        @Query("END_INDEX") endIndex: Int,
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

