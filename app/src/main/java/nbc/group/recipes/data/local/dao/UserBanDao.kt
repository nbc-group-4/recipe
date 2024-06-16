package nbc.group.recipes.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import nbc.group.recipes.data.model.entity.ContentBanEntity
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.model.entity.UserBanEntity

@Dao
interface UserBanDao {
    @Query("SELECT * FROM UserBanEntity")
    fun getAllData(): Flow<List<UserBanEntity>>
    @Insert
    fun insertData(userBanEntity: UserBanEntity): Long
}