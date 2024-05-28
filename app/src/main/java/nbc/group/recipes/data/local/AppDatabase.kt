package nbc.group.recipes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import nbc.group.recipes.data.local.dao.SpecialtyDao
import nbc.group.recipes.data.local.dao.RecipeDao
import nbc.group.recipes.data.model.entity.SpecialtyEntity
import nbc.group.recipes.data.model.entity.RecipeEntity

@Database(entities = [SpecialtyEntity::class, RecipeEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun specialtyDao(): SpecialtyDao
    abstract fun recipeDao(): RecipeDao
}
