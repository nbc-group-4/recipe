package nbc.group.recipes.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
    val recipeImg: String,
    val recipeName: String,
    @PrimaryKey val id: Int,
    val explain: String,
    val step: String,
    val ingredient: String,
    val difficulty: String,
    val time: String,
)
