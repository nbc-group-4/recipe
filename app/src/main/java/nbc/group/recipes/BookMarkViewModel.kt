package nbc.group.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.data.local.dao.RecipeDao
import nbc.group.recipes.data.model.entity.RecipeEntity
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val recipeDao : RecipeDao
) : ViewModel(){

    // Room으로 모든 데이터가져옴
    private val _recipeEntity = MutableStateFlow<List<RecipeEntity>>(emptyList())
    val recipeEntity : StateFlow<List<RecipeEntity>> = _recipeEntity.asStateFlow()

    init {
        viewModelScope.launch {
            recipeDao.getAllData().collect{
                _recipeEntity.value = it
                checkVisiblityView()
            }
        }
    }


    // 데이터유무에 따라 보이는화면 설정하기위해
    private val _visibilityView = MutableStateFlow<VisibilityView>(VisibilityView.EMPTYVIEW)
    val visibilityView: StateFlow<VisibilityView> = _visibilityView.asStateFlow()

    fun checkVisiblityView() {
        if (recipeEntity.value.isEmpty()){
            _visibilityView.value = VisibilityView.EMPTYVIEW
        }else{
            _visibilityView.value = VisibilityView.RECYCLERVIEW
        }
    }


    // 데이터삭제
    fun deleteData(recipeEntity: RecipeEntity) = viewModelScope.launch {
        recipeDao.deleteData(recipeEntity)
    }

}

