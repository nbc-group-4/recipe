package nbc.group.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.KindItem
import nbc.group.recipes.StringsSearch
import nbc.group.recipes.data.model.dto.Item
import nbc.group.recipes.data.repository.RecipeSpecialtyRepository
import javax.inject.Inject

@HiltViewModel
class SpecialtyViewModel @Inject constructor(
    private val repository: RecipeSpecialtyRepository
) : ViewModel() {

    private val _selectedKindItem = MutableStateFlow<KindItem?>(null)
    val selectedKindItem = _selectedKindItem.asStateFlow()

    private val _searchResult = MutableStateFlow<List<Item>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    fun setSelectedKindItem(kindItem: KindItem) {
        _selectedKindItem.value = kindItem
    }

    private fun setSearchResult(result: List<Item>) {
        _searchResult.value = result
    }

    fun searchItem(cntntsSj: String, specialties: List<String>) {
        viewModelScope.launch {
            val specialtyResponse = repository.getSpecialty(null, cntntsSj)
            val filteredItems = specialtyResponse.body.items.item?.filter { item ->
                // Log.e("URGENT_TAG", "searchItem: ${item.areaName}: ${item.cntntsSj}")
                item.cntntsSj?.contains(cntntsSj, ignoreCase = true) == true
                        && item.cntntsSj?.contains(StringsSearch.ITEM1) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM2) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM3) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM4) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM5) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM6) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM7) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM8) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM9) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM10) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM11) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM12) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM13) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM14) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM15) != true
                        && item.cntntsSj?.contains(StringsSearch.ITEM16) != true
                        && specialties.contains(cntntsSj)
            }
            if (filteredItems.isNullOrEmpty()) {
                setSearchResult(emptyList())
            } else {
                setSearchResult(filteredItems)
            }
        }
    }
}