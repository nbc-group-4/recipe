package nbc.group.recipes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.KindItem
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

    fun searchItem(cntntsSj: String) {
        viewModelScope.launch {
            val specialtyResponse = repository.getSpecialty(null, cntntsSj)
            val filteredItems = specialtyResponse.body.items.item?.filter { item ->
                // Log.e("URGENT_TAG", "searchItem: ${item.areaName}: ${item.cntntsSj}")
                item.cntntsSj?.contains(cntntsSj, ignoreCase = true) == true
                        && item.cntntsSj?.contains("(주)") != true
                        && item.cntntsSj?.contains("주식회사") != true
                        && item.cntntsSj?.contains("법인") != true
                        && item.cntntsSj?.contains("조합") != true
                        && item.cntntsSj?.contains("단지") != true
                        && item.cntntsSj?.contains("식품") != true
                        && item.cntntsSj?.contains("곤충") != true
                        && item.cntntsSj?.contains("홍보관") != true
                        && item.cntntsSj?.contains("농협") != true
                        && item.cntntsSj?.contains("농장") != true
                        && item.cntntsSj?.contains("목장") != true
                        && item.cntntsSj?.contains("파인토피아") != true
                        && item.cntntsSj?.contains("건강원") != true
                        && item.cntntsSj?.contains("도자기") != true
                        && item.cntntsSj?.contains("화장품") != true
                        && item.cntntsSj?.contains("주머니") != true
            }
            if (filteredItems.isNullOrEmpty()) {
                setSearchResult(emptyList())
            } else {
                setSearchResult(filteredItems)
            }
        }
    }
}