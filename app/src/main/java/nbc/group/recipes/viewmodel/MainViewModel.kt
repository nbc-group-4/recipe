package nbc.group.recipes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.repository.RecipeSpecialtyRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RecipeSpecialtyRepository
): ViewModel() {

    /**
     * <Sample code>
     * private val _recipes = MutableStateFlow(listOf<Recipe>())
     * val recipes = _recipes.asStateFlow()
     *
     * */


}