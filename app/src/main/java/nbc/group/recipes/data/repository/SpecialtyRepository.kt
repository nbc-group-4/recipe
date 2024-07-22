package nbc.group.recipes.data.repository

import nbc.group.recipes.data.local.dao.RecipeDao
import nbc.group.recipes.data.local.dao.SpecialtyDao
import nbc.group.recipes.data.remote.SpecialtyDataSource
import javax.inject.Inject

class SpecialtyRepository @Inject constructor(
    private val specialtyDataSource: SpecialtyDataSource,
    private val specialtyDao: SpecialtyDao,
    private val recipeDao: RecipeDao
) {

    suspend fun getSpecialty(
        sAreaName: String?,
        cntntsSj: String
    ) = specialtyDataSource.getSpecialty(
        sAreaNm = sAreaName,
        cntntsSj = cntntsSj
    )

    suspend fun getAreaName(areaName: String) = specialtyDataSource.getSpecialty(areaName)

}