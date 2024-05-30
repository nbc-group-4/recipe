package nbc.group.recipes.data.repository

import nbc.group.recipes.data.local.dao.SpecialtyDao
import nbc.group.recipes.data.local.dao.RecipeDao
import nbc.group.recipes.data.remote.SpecialtyDataSource
import nbc.group.recipes.data.remote.RecipeDataSource
import javax.inject.Inject

class RecipeSpecialtyRepository @Inject constructor(
    private val specialtyDataSource: SpecialtyDataSource,
    private val specialtyDao: SpecialtyDao,
    private val recipeDataSource: RecipeDataSource,
    private val recipeDao: RecipeDao
) {
    suspend fun getRecipe() = recipeDataSource.getRecipe()
    suspend fun getSpecialty() = specialtyDataSource.getSpecialty("검색어", "시도", "시군구", 1, 10)
}