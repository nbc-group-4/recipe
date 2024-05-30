package nbc.group.recipes.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SpecialtyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val cntntsNo: Int?,
    val cntntsSj: String?,
    val areaNm: String?,
    val imgUrl: String?,
    val svcDt: Int?,
    val linkUrl: String?
)