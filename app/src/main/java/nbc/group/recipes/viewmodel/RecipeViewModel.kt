package nbc.group.recipes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.BuildConfig
import nbc.group.recipes.convertToOfficial
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeProcedure
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.repository.NaverSearchRepository
import nbc.group.recipes.data.repository.RecipeRepository
import nbc.group.recipes.getRecipeImageUrl
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject


@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val naverSearchRepository: NaverSearchRepository,
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeEntity>?>(null)
    val recipes = _recipes.asStateFlow()

    private val _testRecipes = MutableStateFlow<List<RecipeEntity>?>(null)
    val testRecipe = _testRecipes.asStateFlow()

    private fun encodeToUtf8(query: String): String {
        val convertQuery = convertToOfficial(query)
        return URLEncoder.encode(convertQuery, StandardCharsets.UTF_8.toString())
    }

    suspend fun fetchRecipeIds(ingredientName: String, startIndex: Int, endIndex: Int): List<Int> {
        val ingredientResponse =
            recipeRepository.getRecipeIngredients(startIndex, endIndex, ingredientName)
        return ingredientResponse.row.map { it.recipeId }
    }

    suspend fun fetchRecipeIdsV2(ingredientName: String) = viewModelScope.launch(Dispatchers.IO) {

        val temp1 = async { recipeRepository.getRecipeIngredientsV2(ingredientName) }
        val recipeIds = temp1.await().recipeIngredient.row

        val recipeEntityList = mutableListOf<RecipeEntity>()

        recipeIds.forEach { recipeIngredient ->
            val temp2 = async { recipeRepository.getRecipesV2(recipeIngredient.recipeId) }
            val recipeWrapper = temp2.await()
            if (recipeWrapper.recipe.totalCnt >= 1) {
                val recipe = recipeWrapper.recipe.row[0]

//                val encodedRecipeName = URLEncoder.encode(recipe.recipeName, StandardCharsets.UTF_8.toString())
//                val temp3 = async {
//                    naverSearchRepository.searchEncyclopedia(
//                        encodedRecipeName,
//                        BuildConfig.NAVER_CLIENT_ID,
//                        BuildConfig.NAVER_CLIENT_SECRET
//                    )
//                }
//                val imageResponse = temp3.await()
//
//                val temp4 = imageResponse.items.firstOrNull()
//                val temp5 = temp4?.thumbnail
                val temp5 = getRecipeImageUrl(recipe.recipeName)

                val temp6 = async { recipeRepository.getRecipeProceduresV2(recipe.recipeId) }


                val gson = Gson()
                val procedure = gson.toJson(temp6.await().recipeProcedure.row)

                recipeEntityList.add(
                    RecipeEntity(
                        id = recipe.recipeId,
                        recipeImg = temp5?: "",
                        recipeName = recipe.recipeName,
                        explain = recipe.summary,
                        step = procedure,
                        ingredient = ingredientName,
                        difficulty = recipe.levelName,
                        time = recipe.cookingTime
                    )
                )
            }
        }

        _testRecipes.emit(recipeEntityList)
    }


    fun putRecipeEntity(recipeEntity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        recipeRepository.putRecipeEntity(recipeEntity)
    }

    fun getRecipeDetails(
        startIndex: Int,
        endIndex: Int,
        recipeName: String,
        recipeId: Int,
        clientId: String,
        clientSecret: String,
    ) = viewModelScope.launch {
        try {
            val recipes = mutableListOf<RecipeEntity>()
            val recipeDeferred =
                async { recipeRepository.getRecipes(startIndex, endIndex, "", recipeId) }
            val recipeResponse = recipeDeferred.await()
            Log.e("TAG", "getRecipeDetails: recipeDeferred: $recipeResponse")

            val recipe = recipeResponse.row.first()
            val recipeName = recipe.recipeName
            val encodedRecipeName = encodeToUtf8(recipeName)
            val imageDeferred = async {
                naverSearchRepository.searchEncyclopedia(
                    encodedRecipeName,
                    clientId,
                    clientSecret
                )
            }
            val imageResponse = imageDeferred.await()

            val firstItem = imageResponse.items.firstOrNull()
            val thumbnailUrl = firstItem?.thumbnail ?: ""

            val procedureDeferred =
                async { recipeRepository.getRecipeProcedures(startIndex, endIndex, recipeId) }
            val procedureResponse = procedureDeferred.await()
            val procedure = procedureResponse.row.firstOrNull().toString()

            val ingredientDeferred =
                async { recipeRepository.getRecipeIngredients(startIndex, endIndex, recipeId) }
            val ingredientResponse = ingredientDeferred.await()
            val ingredients = ingredientResponse.row.firstOrNull().toString()

            recipes.add(
                RecipeEntity(
                    id = recipe.recipeId,
                    recipeImg = thumbnailUrl,
                    recipeName = recipe.recipeName,
                    explain = recipe.summary,
                    step = procedure,
                    ingredient = ingredients,
                    difficulty = recipe.levelName,
                    time = recipe.cookingTime
                )
            )
            _recipes.emit(recipes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}