package nbc.group.recipes.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long
)
