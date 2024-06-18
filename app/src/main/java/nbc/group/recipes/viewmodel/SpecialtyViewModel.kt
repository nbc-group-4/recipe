package nbc.group.recipes.viewmodel

import android.util.Log
import android.widget.Toast
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
            }
            if (filteredItems.isNullOrEmpty()) {
                setSearchResult(emptyList())
                // Toast.makeText(this, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                setSearchResult(filteredItems)
            }
        }
    }
}