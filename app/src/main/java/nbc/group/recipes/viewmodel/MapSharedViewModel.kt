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
    private val _specialtie = MutableStateFlow<List<Item>?>(null)
    val specialtie : StateFlow<List<Item>?> = _specialtie

    fun getSpecialtie(ariaName : String) = viewModelScope.launch {
        val getAriaData = repository.getAreaName(ariaName)
        val filteredItems = getAriaData.body.items.item?.filter {
            // 제거해야되는 특산물데이터 필터링(추후에 더 추가예정)
            when(ariaName){
                "양주" -> it.cntntsSj != "반려식물"
                "군산" -> it.cntntsSj != "바다향"
                else -> true
            }
        }
        _specialtie.emit(filteredItems)
    }



    // 클릭한 특산물 데이터
    private val _selectedSpecialty = MutableStateFlow<Item?>(null)
    val selectedSpecialty: StateFlow<Item?> = _selectedSpecialty

    fun getSelectedSpecialty(item: Item?) {
        _selectedSpecialty.value = item
    }

}