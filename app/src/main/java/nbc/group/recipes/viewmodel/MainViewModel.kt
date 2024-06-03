package nbc.group.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.data.model.dto.Item
import nbc.group.recipes.data.network.SpecialtyService
import nbc.group.recipes.data.repository.RecipeSpecialtyRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RecipeSpecialtyRepository
) : ViewModel() {

    /**
     * <Sample code>
     * private val _recipes = MutableStateFlow(listOf<Recipe>())
     * val recipes = _recipes.asStateFlow()
     *
     * */

    private val _specialties = MutableStateFlow<List<Item>?>(null)
    val specialties = _specialties.asStateFlow()

    fun doTest(specialtyType: String) {
        viewModelScope.launch {
            val items = repository.getSpecialty("부산").body.items.item
            _specialties.emit(items)
        }
    }

    fun getSpecialties(type: String) {
        viewModelScope.launch {
//            val response = repository.getSpecialty(type)
//            _specialties.emit(response)
        }
    }
}