package nbc.group.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.convertToOfficial
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeProcedure
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.repository.NaverSearchRepository
import nbc.group.recipes.data.repository.RecipeRepository
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

    private fun encodeToUtf8(query: String): String {
        val convertQuery = convertToOfficial(query)
        return URLEncoder.encode(convertQuery, StandardCharsets.UTF_8.toString())
    }

    suspend fun fetchRecipeIds(ingredientName: String, startIndex: Int, endIndex: Int): List<Int> {
        val ingredientResponse =
            recipeRepository.getRecipeIngredients(startIndex, endIndex, ingredientName)
        return ingredientResponse.row.map { it.recipeId }
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
        clientSecret: String
    ) = viewModelScope.launch {
        try {
            val recipes = mutableListOf<RecipeEntity>()
            val recipeDeferred = async { recipeRepository.getRecipes(startIndex, endIndex, "", recipeId) }
            val recipeResponse = recipeDeferred.await()

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

            val procedureDeferred = async { recipeRepository.getRecipeProcedures(startIndex, endIndex, recipeId) }
            val procedureResponse = procedureDeferred.await()
            val procedure = procedureResponse.row.firstOrNull().toString()

            val ingredientDeferred = async { recipeRepository.getRecipeIngredients(startIndex, endIndex, recipeId) }
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