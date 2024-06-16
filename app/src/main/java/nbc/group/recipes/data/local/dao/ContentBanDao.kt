package nbc.group.recipes.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import nbc.group.recipes.data.model.entity.ContentBanEntity
import nbc.group.recipes.data.model.entity.RecipeEntity

@Dao
interface ContentBanDao {
    @Query("SELECT * FROM ContentBanEntity")
    fun getAllData(): Flow<List<ContentBanEntity>>
    @Insert
    fun insertData(contentBanEntity: ContentBanEntity): Long

    @Query("INSERT INTO ContentBanEntity(target) VALUES(:target)")
    fun insertData(target: String): Long
}