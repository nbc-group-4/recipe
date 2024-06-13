package nbc.group.recipes.data.repository

import nbc.group.recipes.data.local.dao.RecipeDao
import nbc.group.recipes.data.local.dao.SpecialtyDao
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.remote.SpecialtyDataSource
import javax.inject.Inject

class RecipeSpecialtyRepository @Inject constructor(
    private val specialtyDataSource: SpecialtyDataSource,
    private val specialtyDao: SpecialtyDao,
    private val recipeDao: RecipeDao
) {


//    suspend fun getRecipe(recipeName: String) = recipeDataSource.getRecipe(
//        recipeName = recipeName,
//        recipeId = 0
//    )

    suspend fun getSpecialty(
        sAreaName: String?,
        cntntsSj: String
    ) = specialtyDataSource.getSpecialty(
        sAreaNm = sAreaName,
        cntntsSj = cntntsSj
    )

    suspend fun getAreaName(areaName: String) = specialtyDataSource.getSpecialty(areaName)

}