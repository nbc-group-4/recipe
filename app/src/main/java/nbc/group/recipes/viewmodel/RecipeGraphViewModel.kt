package nbc.group.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nbc.group.recipes.data.model.dto.Item
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.network.NetworkResult
import nbc.group.recipes.data.repository.FirebaseRepository
import nbc.group.recipes.data.repository.RecipeRepository
import nbc.group.recipes.presentation.graph.Edge
import nbc.group.recipes.presentation.graph.Node
import javax.inject.Inject

@HiltViewModel
class RecipeGraphViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private val mutex = Mutex()

    private var count = 0
    private val num = 10

    private val _currentSpecialty = MutableStateFlow<Item?>(null)
    val currentSpecialty = _currentSpecialty.asStateFlow()

    private val _recipes = MutableStateFlow<NetworkResult<List<RecipeEntity>>?>(null)
    val recipes = _recipes.asStateFlow()

    private val _lottieSwitch = MutableStateFlow(true)
    val lottieSwitch = _lottieSwitch.asStateFlow()

    private val _nodes = MutableStateFlow(listOf<Node<Any>>())
    val nodes = _nodes.asStateFlow()

    private val _edges = MutableStateFlow(listOf<Edge>())
    val edges = _edges.asStateFlow()

    var history: Item? = null

    fun setCurrentSpecialty(item: Item) {
        viewModelScope.launch {
            _recipes.emit(null)
            _nodes.emit(emptyList())
            _edges.emit(emptyList())
            count = 0
            _currentSpecialty.emit(item)
        }
    }

    fun initRecipeFlow() {
        _recipes.value = null
    }

    fun getRecipes(
        ingredient: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        mutex.withLock {
            val temp1 = recipeRepository.getRecipesV4(
                count * num + 1,
                (count + 1) * num,
                ingredient
            )
            count++

            val temp2 = firebaseRepository.getRecipeForTestV2(ingredient)

            if(temp1 is NetworkResult.Success && temp2 is NetworkResult.Success) {
                val temp3 = mutableListOf<RecipeEntity>()
                temp3.addAll(temp1.result)
                temp3.addAll(temp2.result)
                _recipes.emit(
                    NetworkResult.Success(temp3)
                )
            }

            if(temp1 is NetworkResult.Failure || temp2 is NetworkResult.Failure) {
                _recipes.emit(
                    NetworkResult.Failure(Exception())
                )
            }

            // _recipes.emit(temp1)
            _lottieSwitch.emit(!_lottieSwitch.value)
        }
    }

    fun storeGraphElem(
        nodes: List<Node<Any>>,
        edges: List<Edge>
    ) {
        _nodes.value = nodes
        _edges.value = edges
    }
}
