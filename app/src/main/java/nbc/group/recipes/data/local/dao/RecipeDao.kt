package nbc.group.recipes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nbc.group.recipes.data.model.entity.RecipeEntity

@Dao
interface RecipeDao {
    @Query("SELECT * FROM RecipeEntity")
    fun getAllData(): Flow<List<RecipeEntity>>
    @Insert
    fun insertData(recipeEntity: RecipeEntity): Long
    @Update
    fun updateData(recipeEntity: RecipeEntity)
    @Delete
    fun deleteData(recipeEntity: RecipeEntity)
    @Query("SELECT * FROM RecipeEntity WHERE id = :id")
    fun getDataByIdForAPI(id: Int): List<RecipeEntity>
    @Query("SELECT * FROM RecipeEntity WHERE firebaseId = :id")
    fun getDataByIdForFirebase(id: String): List<RecipeEntity>

}
