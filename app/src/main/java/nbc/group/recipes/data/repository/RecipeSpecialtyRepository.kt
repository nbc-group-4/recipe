package nbc.group.recipes.data.repository

import nbc.group.recipes.data.local.dao.SpecialtyDao
import nbc.group.recipes.data.remote.SpecialtyDataSource
import javax.inject.Inject

class RecipeSpecialtyRepository @Inject constructor(
    private val specialtyDataSource: SpecialtyDataSource,
    private val specialtyDao: SpecialtyDao
) {


//    suspend fun getRecipe(recipeName: String) = recipeDataSource.getRecipe(
//        recipeName = recipeName,
//        recipeId = 0
//    )

    suspend fun getSpecialty(cntntsSj: String) = specialtyDataSource.getSpecialty(
        sAreaNm = "sAreaNm",
        cntntsSj = cntntsSj
    )
}
