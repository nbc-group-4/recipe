package nbc.group.recipes.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

//@HiltViewModel
//class MypageSharedViewModel @Inject constructor() : ViewModel() {
//    private val _selectedRecipe = MutableStateFlow<String?>(null)
//    val selectedRecipe: StateFlow<String?> = _selectedRecipe
//    fun selectRecipe(recipeId: String) {
//        _selectedRecipe.value = recipeId
//    }
//}