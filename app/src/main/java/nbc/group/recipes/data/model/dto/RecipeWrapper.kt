package nbc.group.recipes.data.model.dto

import com.google.gson.annotations.SerializedName

data class RecipeIngredientWrapper(
    @SerializedName("Grid_20150827000000000227_1")
    val recipeIngredient: RecipeResponse<RecipeIngredient>
)

data class RecipeWrapper(
    @SerializedName("Grid_20150827000000000226_1")
    val recipe: RecipeResponse<Recipe>
)

data class RecipeProcedureWrapper(
    @SerializedName("Grid_20150827000000000228_1")
    val recipeProcedure: RecipeResponse<RecipeProcedure>
)

