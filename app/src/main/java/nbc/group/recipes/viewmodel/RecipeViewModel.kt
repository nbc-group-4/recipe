package nbc.group.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeProcedure
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.repository.RecipeRepository
import javax.inject.Inject

class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
): ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeEntity>?>(null)
    val recipes = _recipes.asStateFlow()

    private val _recipeIngredients = MutableStateFlow<List<RecipeIngredient>?>(null)
    val recipeIngredients = _recipeIngredients.asStateFlow()

    private val _recipeProcedures = MutableStateFlow<List<RecipeProcedure>?>(null)
    val recipeProcedures = _recipeProcedures.asStateFlow()

    fun getRecipes(startIndex: Int, endIndex: Int, recipeName: String, recipeId: Int) = viewModelScope.launch {
        val response = recipeRepository.getRecipes(startIndex, endIndex, recipeName, recipeId)
        _recipes.emit(response.row.map { recipe: Recipe ->
            RecipeEntity(
                id = recipe.recipeId,
                recipeImg = recipe.calorie, // 실제 레시피 이미지 필드로 변경해야 함
                recipeName = recipe.recipeName,
                explain = recipe.summary,
                step = "",
                ingredient = "",
                difficulty = recipe.levelName,
                time = recipe.cookingTime
            )
        })
    }

    fun getRecipeIngredients(startIndex: Int, endIndex: Int, ingredient: String, recipeId: Int) = viewModelScope.launch {
        val response = recipeRepository.getRecipeIngredients(startIndex, endIndex, ingredient, recipeId)
        _recipeIngredients.emit(response.row)
    }

    fun getRecipeProcedures(startIndex: Int, endIndex: Int, recipeId: Int) = viewModelScope.launch {
        val response = recipeRepository.getRecipeProcedures(startIndex, endIndex, recipeId)
        _recipeProcedures.emit(response.row)
    }

}