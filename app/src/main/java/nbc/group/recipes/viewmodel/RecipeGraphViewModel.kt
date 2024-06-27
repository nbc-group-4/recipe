package nbc.group.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nbc.group.recipes.data.model.dto.Item
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.network.NetworkResult
import nbc.group.recipes.data.repository.RecipeRepository
import javax.inject.Inject

@HiltViewModel
class RecipeGraphViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
): ViewModel() {

    private val mutex = Mutex()
    private var count = 0
    private val num = 10

    private val _currentSpecialty = MutableStateFlow<Item?>(null)
    val currentSpecialty = _currentSpecialty.asStateFlow()

    private val _recipes = MutableStateFlow<NetworkResult<List<RecipeEntity>>?>(null)
    val recipes = _recipes.asStateFlow()

    private val _lottieSwitch = MutableStateFlow(true)
    val lottieSwitch = _lottieSwitch.asStateFlow()

    fun setCurrentSpecialty(item: Item) {
        viewModelScope.launch {
            _currentSpecialty.emit(item)
        }
    }

    fun getRecipes(
        ingredient: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        mutex.withLock {
            val temp = recipeRepository.getRecipesV4(
                count * num + 1,
                (count + 1) * num,
                ingredient
            )
            count++
            _recipes.emit(temp)
            _lottieSwitch.emit(!_lottieSwitch.value)
        }
    }
}
