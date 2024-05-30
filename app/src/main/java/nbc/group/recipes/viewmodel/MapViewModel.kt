package nbc.group.recipes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nbc.group.recipes.data.model.dto.SearchResponse
import nbc.group.recipes.data.repository.SearchRepository
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    // StateFlow로 바꿀예정
    private val _regionSearch = MutableLiveData<SearchResponse>()
    val regionSearch : LiveData<SearchResponse> = _regionSearch

    fun getregionSearch(query: String) = viewModelScope.launch {
        _regionSearch.value = searchRepository.requestSearch(query)
    }

}