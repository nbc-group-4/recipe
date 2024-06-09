package nbc.group.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.convertToOfficial
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeProcedure
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.repository.RecipeRepository
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlinx.coroutines.awaitAll

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
): ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeEntity>?>(null)
    val recipes = _recipes.asStateFlow()

    private val _recipeIngredients = MutableStateFlow<List<RecipeIngredient>?>(null)
    val recipeIngredients = _recipeIngredients.asStateFlow()

    private val _recipeProcedures = MutableStateFlow<List<RecipeProcedure>?>(null)
    val recipeProcedures = _recipeProcedures.asStateFlow()

    private fun encodeToUtf8(query: String): String {
        val convertQuery = convertToOfficial(query)
        return URLEncoder.encode(convertQuery, StandardCharsets.UTF_8.toString())
    }

//    fun getRecipeDetails(startIndex: Int, endIndex: Int, recipeName: String, recipeId: Int, clientId: String, clientSecret: String) = viewModelScope.launch{
//        try {
//            val ingredientName = "논살딸기" // 바텀 시트에서 클릭된 재료명
//            val encodedRecipeName = encodeToUtf8(ingredientName)
//
//            val ingredientResponse = recipeRepository.getRecipeIngredients(startIndex, endIndex, encodedRecipeName, recipeId)
//            val recipeIds = ingredientResponse.row.map { it.recipeId }
//
//            val recipes = mutableListOf<RecipeEntity>()
//
//            for (id in recipeIds) {
//                val recipeDeferred = async { recipeRepository.getRecipes(startIndex, endIndex, "", id) }
//                val recipeResponse = recipeDeferred.await()
//
//                val recipe = recipeResponse.row.first()
//                val recipeName = recipe.recipeName
//                val encodedRecipeName = encodeToUtf8(recipeName)
//                val imageDeferred = async { naverSearchRepository.searchEncyclopedia(encodedRecipeName, clientId, clientSecret) }
//                val imageResponse = imageDeferred.await()
//
//                val firstItem = imageResponse.items.firstOrNull()
//                val thumbnailUrl = firstItem?.thumbnail ?: ""
//
//                recipes.add(
//                    RecipeEntity(
//                        id = recipe.recipeId,
//                        recipeImg = thumbnailUrl,
//                        recipeName = recipe.recipeName,
//                        explain = recipe.summary,
//                        step = "",
//                        ingredient = "",
//                        difficulty = recipe.levelName,
//                        time = recipe.cookingTime
//                    )
//                )
//            }
//            _recipes.emit(recipes)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    fun getRecipeIngredients(startIndex: Int, endIndex: Int, ingredient: String, recipeId: Int) = viewModelScope.launch {
        val response = recipeRepository.getRecipeIngredients(startIndex, endIndex, ingredient, recipeId)
        _recipeIngredients.emit(response.row)
    }

    fun getRecipeProcedures(startIndex: Int, endIndex: Int, recipeId: Int) = viewModelScope.launch {
        val response = recipeRepository.getRecipeProcedures(startIndex, endIndex, recipeId)
        _recipeProcedures.emit(response.row)
    }

}