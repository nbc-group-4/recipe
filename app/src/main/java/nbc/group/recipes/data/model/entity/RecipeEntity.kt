package nbc.group.recipes.data.model.entity

import androidx.room.Entity

@Entity
data class RecipeEntity(
    val recipeImg: String,
    val recipeName: String,
    val id: Int,
    val explain: String,
    val step: String,
    val ingredient: String,
    val difficulty: String,
)
