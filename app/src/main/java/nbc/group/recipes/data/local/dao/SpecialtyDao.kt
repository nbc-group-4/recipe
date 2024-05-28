package nbc.group.recipes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nbc.group.recipes.data.model.entity.SpecialtyEntity

@Dao
interface SpecialtyDao {
    @Query("SELECT * FROM SpecialtyEntity")
    fun getAllData(): Flow<SpecialtyEntity>
    @Insert
    fun insertData(specialtyEntity: SpecialtyEntity): Long
    @Update
    fun updateData(specialtyEntity: SpecialtyEntity)
    @Delete
    fun deleteData(specialtyEntity: SpecialtyEntity)
}