package nbc.group.recipes.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import nbc.group.recipes.data.model.dto.SpecialtyItem
import retrofit2.http.GET
import retrofit2.http.Query

@Entity
data class SpecialtyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val total: Int?,
    val start: Int?,
    val display: Int?,
    val item: List<SpecialtyItem>?
)

@Entity
data class SpecialtyItemEntity(
    val title: String,
    val link: String,
    val image: String,
    val category1: String,
    val category2: String,
    val ingredientName: String,
    val ingredientName2: String
)