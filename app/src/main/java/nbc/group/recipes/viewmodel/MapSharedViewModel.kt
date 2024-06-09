package nbc.group.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.data.model.dto.Item
import nbc.group.recipes.data.model.dto.SpecialtyResponse
import nbc.group.recipes.data.repository.RecipeSpecialtyRepository
import javax.inject.Inject

@HiltViewModel
class MapSharedViewModel @Inject constructor(
    private val repository: RecipeSpecialtyRepository,
) : ViewModel(){


    // 지역이름
    private val _specialtie = MutableStateFlow<SpecialtyResponse?>(null)
    val specialtie : StateFlow<SpecialtyResponse?> = _specialtie

    fun getSpecialtie(ariaName : String) = viewModelScope.launch {
        _specialtie.emit(repository.getAreaName(ariaName))
    }



    // 클릭한 특산물 데이터
    private val _selectedSpecialty = MutableStateFlow<Item?>(null)
    val selectedSpecialty: StateFlow<Item?> = _selectedSpecialty

    fun getSelectedSpecialty(item: Item?) {
        _selectedSpecialty.value = item
    }

}