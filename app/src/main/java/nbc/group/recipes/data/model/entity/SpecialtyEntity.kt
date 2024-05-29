package nbc.group.recipes.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

@Entity
data class SpecialtyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val total: Int?,
    val start: Int?,
    val display: Int?,
)