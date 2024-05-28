package nbc.group.recipes.data.model.dto

import com.google.gson.annotations.SerializedName

data class RecipeResponse<T> (
    val totalCnt: Int,
    val startRow: Int,
    val endRow: Int,
    val result: MetaResult,
    val row: List<T>
)

data class MetaResult(
    val code: String,
    val message: String
)