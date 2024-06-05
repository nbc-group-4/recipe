package nbc.group.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.KakaoMap.OnLabelClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.data.model.dto.SearchResponse
import nbc.group.recipes.data.model.dto.SpecialtyResponse
import nbc.group.recipes.data.repository.RecipeSpecialtyRepository
import nbc.group.recipes.data.repository.SearchRepository
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val repository: RecipeSpecialtyRepository,
) : ViewModel() {

    // 검색어
    private val _regionSearch = MutableStateFlow<SearchResponse?>(null)
    val regionSearch : StateFlow<SearchResponse?> = _regionSearch

    fun getRegionSearch(query: String) = viewModelScope.launch {
        _regionSearch.emit(searchRepository.requestSearch(query))
    }



    // 지역이름
    private val _specialtie = MutableStateFlow<SpecialtyResponse?>(null)
    val specialtie : StateFlow<SpecialtyResponse?> = _specialtie

    fun getSpecialtie(ariaName : String) = viewModelScope.launch {
            _specialtie.emit(repository.getSpecialty(ariaName))
        }

}