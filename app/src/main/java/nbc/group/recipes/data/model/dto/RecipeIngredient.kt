package nbc.group.recipes.data.model.dto

import com.google.gson.annotations.SerializedName

data class RecipeIngredient(
    @SerializedName("ROW_NUM") val rowNum: Int,
    @SerializedName("RECIPE_ID") val recipeId: Int,
    @SerializedName("IRDNT_SN") val ingredientSequenceNumber: Int,
    @SerializedName("IRDNT_NM") val ingredientName: String,
    @SerializedName("IRDNT_CPCTY") val ingredientCapacity: String,
    @SerializedName("IRDNT_TY_CODE") val ingredientTypeCode: String,
    @SerializedName("IRDNT_TY_NM") val ingredientTypeName: String,
)
