package nbc.group.recipes.data.model.dto

import com.google.gson.annotations.SerializedName

data class RecipeProcedure(
    @SerializedName("ROW_NUM") val rowNum: Int,
    @SerializedName("RECIPE_ID") val recipeId: Int,
    @SerializedName("COOKING_NO") val cookingNumber: Int,
    @SerializedName("COOKING_DC") val cookingDescription: String,
    @SerializedName("STEP_TIP") val tip: String,

)
