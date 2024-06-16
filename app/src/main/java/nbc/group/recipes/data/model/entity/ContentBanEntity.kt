package nbc.group.recipes.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContentBanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = -1,
    val target: String,
)
