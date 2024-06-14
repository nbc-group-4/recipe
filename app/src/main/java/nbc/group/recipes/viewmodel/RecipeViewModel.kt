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
import nbc.group.recipes.convertToOfficial
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.repository.FirebaseRepository
import nbc.group.recipes.data.repository.NaverSearchRepository
import nbc.group.recipes.data.repository.RecipeRepository
import nbc.group.recipes.getRecipeImageUrl
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject


@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val firebaseRepository: FirebaseRepository,
    private val naverSearchRepository: NaverSearchRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeEntity>?>(null)
    val recipes = _recipes.asStateFlow()

    /**
     * API를 통해서 가져온 레시피의 경우 step이 RecipeProcedure type이다.
     * 유저가 만든 레시피의 경우 일반 string 이다.
     * */
    suspend fun fetchRecipe(ingredientName: String) = viewModelScope.launch(Dispatchers.IO) {

        val temp1 = async { recipeRepository.getRecipeIngredientsV2(ingredientName) }
        val recipeIds = temp1.await().recipeIngredient.row

        val recipeEntityList = mutableListOf<RecipeEntity>()

        recipeIds.forEach { recipeIngredient ->
            val temp2 = async { recipeRepository.getRecipesV2(recipeIngredient.recipeId) }
            val recipeWrapper = temp2.await()
            if (recipeWrapper.recipe.totalCnt >= 1) {
                val recipe = recipeWrapper.recipe.row[0]
                val temp3 = getRecipeImageUrl(recipe.recipeName)
                val temp4 = async { recipeRepository.getRecipeProceduresV2(recipe.recipeId) }
                val gson = Gson()
                val procedure = gson.toJson(temp4.await().recipeProcedure.row)
                recipeEntityList.add(
                    RecipeEntity(
                        id = recipe.recipeId,
                        recipeImg = temp3?: "",
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


//        firebaseRepository.getRecipesByIngredient(ingredientName)


        _recipes.emit(recipeEntityList)
    }



    fun putRecipeEntity(recipeEntity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        recipeRepository.putRecipeEntity(recipeEntity)
    }



    private fun encodeToUtf8(query: String): String {
        val convertQuery = convertToOfficial(query)
        return URLEncoder.encode(convertQuery, StandardCharsets.UTF_8.toString())
    }

    suspend fun fetchRecipeIds(ingredientName: String, startIndex: Int, endIndex: Int): List<Int> {
        val ingredientResponse =
            recipeRepository.getRecipeIngredients(startIndex, endIndex, ingredientName)
        return ingredientResponse.row.map { it.recipeId }
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