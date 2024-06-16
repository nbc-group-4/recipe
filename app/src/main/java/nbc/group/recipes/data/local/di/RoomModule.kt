package nbc.group.recipes.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nbc.group.recipes.data.local.AppDatabase
import nbc.group.recipes.data.local.dao.ContentBanDao
import nbc.group.recipes.data.local.dao.SpecialtyDao
import nbc.group.recipes.data.local.dao.RecipeDao
import nbc.group.recipes.data.local.dao.UserBanDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app.db"
    ).build()

    @Singleton
    @Provides
    fun provideAgricultureDao(
        appDatabase: AppDatabase
    ): SpecialtyDao = appDatabase.specialtyDao()

    @Singleton
    @Provides
    fun provideRecipeDao(
        appDatabase: AppDatabase
    ): RecipeDao = appDatabase.recipeDao()

    @Singleton
    @Provides
    fun provideContentBanDao(
        appDatabase: AppDatabase
    ): ContentBanDao = appDatabase.contentBanDao()

    @Singleton
    @Provides
    fun provideUserBanDao(
        appDatabase: AppDatabase
    ): UserBanDao = appDatabase.userBanDao()
}
