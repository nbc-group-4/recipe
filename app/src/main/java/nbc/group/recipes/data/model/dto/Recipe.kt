package nbc.group.recipes.data.model.dto

import com.google.gson.annotations.SerializedName
import nbc.group.recipes.data.model.entity.RecipeEntity

data class Recipe(
    @SerializedName("ROW_NUM") val rowNum: Int,
    @SerializedName("RECIPE_ID") val recipeId: Int,
    @SerializedName("RECIPE_NM_KO") val recipeName: String,
    @SerializedName("SUMRY") val summary: String,
    @SerializedName("NATION_CODE") val nationCode: String,
    @SerializedName("NATION_NM") val nationName: String,
    @SerializedName("TY_CODE") val typeCode: String,
    @SerializedName("TY_NM") val typeName: String,
    @SerializedName("COOKING_TIME") val cookingTime: String,
    @SerializedName("CALORIE") val calorie: String,
    @SerializedName("QNT") val quantity: String,
    @SerializedName("LEVEL_NM") val levelName: String,
    @SerializedName("IRDNT_CODE") val ingredientCode: String,
    @SerializedName("PC_NM") val price: String,
) {
    constructor(): this(
        1, 1, "a", "b", "c", "d",
        "e", "f", "g", "h", "i", "j",
        "k", "l")
}


// RecipeEntity를 Recipe로 변환하는 확장함수
fun RecipeEntity.toRecipe() = Recipe(
    rowNum = 0,
    recipeId = id,
    recipeName = recipeName,
    summary = explain,
    nationCode = "",
    nationName = ingredient,
    typeCode = "",
    typeName = "",
    cookingTime = time,
    calorie = "",
    quantity = "",
    levelName = difficulty,
    ingredientCode = "",
    price = ""
)
