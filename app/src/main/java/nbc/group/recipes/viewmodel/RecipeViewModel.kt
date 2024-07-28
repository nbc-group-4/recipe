package nbc.group.recipes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nbc.group.recipes.convertToOfficial
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.entity.ContentBanEntity
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.model.entity.UserBanEntity
import nbc.group.recipes.data.network.NetworkResult
import nbc.group.recipes.data.repository.BanRepository
import nbc.group.recipes.data.repository.FirebaseRepository
import nbc.group.recipes.data.repository.NaverSearchRepository
import nbc.group.recipes.data.repository.RecipeRepository
import nbc.group.recipes.getRecipeImageUrl
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject


@HiltViewModel // -> 해당 ViewModel에 의존성을 주입하여 ViewModel이 필요한 Repository나 다른 클래스의 인스턴스를 Hilt가 자동으로 주입함
// -> Hilt는 ViewModel의 생명주기를 관리하기 때문에 viewModelScope를 사용하여 코루틴을 안전하게 사용 가능
class RecipeViewModel @Inject constructor(
    // @inject -> 필요한 의존성을 ViewModel의 생성자에 주입함
    private val recipeRepository: RecipeRepository,
    private val firebaseRepository: FirebaseRepository,
    private val naverSearchRepository: NaverSearchRepository,
    private val banRepository: BanRepository, // <- 자동으로 주입되는 Repository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeEntity>?>(null)
    val recipes = _recipes.asStateFlow()

    private val _firebaseRecipes = MutableStateFlow<NetworkResult<List<Recipe>>?>(null)
    val firebaseRecipes = _firebaseRecipes.asStateFlow()

    private val _contentBan = MutableStateFlow<List<ContentBanEntity>>(listOf())
    val contentBan = _contentBan.asStateFlow()

    private val _userBan = MutableStateFlow<List<UserBanEntity>>(listOf())
    val userBan = _userBan.asStateFlow()

    private val _recipeProcedure = MutableStateFlow("")
    val recipeProcedure = _recipeProcedure.asStateFlow()

    private val _ingredients = MutableStateFlow<List<String>>(emptyList())
    val ingredients = _ingredients.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                banRepository.getAllContentBan().collectLatest { contentBan ->
                    val bannedList = mutableListOf<String>()
                    contentBan.forEach { bannedList.add(it.target) }
                    _recipes.value?.let { nonNull ->
                        _recipes.emit(
                            nonNull.filter { recipeEntity ->
                                recipeEntity.firebaseId !in bannedList
                            }
                        )
                    }
                }
            }
            launch {
                banRepository.getAllUserBan().collectLatest { userBan ->
                    val bannedList = mutableListOf<String>()
                    userBan.forEach { bannedList.add(it.target) }
                    _recipes.value?.let { nonNull ->
                        _recipes.emit(
                            nonNull.filter { recipeEntity ->
                                recipeEntity.writerId !in bannedList
                            }
                        )
                    }
                }
            }
        }
    }

    fun getRecipeProcedure(
        recipeEntity: RecipeEntity
    ) = viewModelScope.launch {
        val temp = recipeRepository.getRecipeProceduresV2(recipeEntity.id)
        Log.e("URGENT_TAG", "getRecipeProcedure: $temp")
        val temp1 = temp.recipeProcedure
        var result = ""
        temp1.row.forEach {
            result += it.cookingDescription
        }
        _recipeProcedure.emit(result)
    }

    suspend fun getRecipeStep(recipeEntity: RecipeEntity) =
        recipeRepository.getRecipeProceduresV2(recipeEntity.id)

    fun recipeUpdate(recipes: List<RecipeEntity>) {
        viewModelScope.launch {
            _recipes.emit(recipes)
        }
    }

    fun insertContentBan(contentId: String) {
        banRepository.insertContentBan(contentId)
    }

    fun insertUserBan(userId: String) {
        banRepository.insertUserBan(
            UserBanEntity(target = userId)
        )
    }

    fun test() {
        viewModelScope.launch {
            firebaseRepository.getRecipeForTest("치즈")
        }
    }

    fun clearRecipes() {
        viewModelScope.launch {
            _recipes.emit(null)
        }
    }

    fun mergeFirebaseData(recipeEntities: List<RecipeEntity>) = viewModelScope.launch {
        _recipes.emit(recipeEntities)
    }

    /**
     * API를 통해서 가져온 레시피의 경우 step이 RecipeProcedure type이다.
     * 유저가 만든 레시피의 경우 일반 string 이다.
     * */
    suspend fun fetchRecipe(ingredientName: String) = viewModelScope.launch(Dispatchers.IO) {

        val temp1 = async { recipeRepository.getRecipeIngredientsV2(ingredientName) }
        val recipeIds = temp1.await().recipeIngredient.row

        val recipeEntityList = mutableListOf<RecipeEntity>()

        recipeIds.forEach { recipeIngredient ->
            val temp2 = async { recipeRepository.getRecipesV2(recipeIngredient.recipeId) }
            val recipeWrapper = temp2.await()
            if (recipeWrapper.recipe.totalCnt >= 1) {
                val recipe = recipeWrapper.recipe.row[0]
                val temp3 = getRecipeImageUrl(recipe.recipeName)
                val temp4 = async { recipeRepository.getRecipeProceduresV2(recipe.recipeId) }
                val gson = Gson()
                val procedure = gson.toJson(temp4.await().recipeProcedure.row)
                recipeEntityList.add(
                    RecipeEntity(
                        id = recipe.recipeId,
                        recipeImg = temp3 ?: "",
                        recipeName = recipe.recipeName,
                        explain = recipe.summary,
                        step = procedure,
                        ingredient = ingredientName,
                        difficulty = recipe.levelName,
                        time = recipe.cookingTime,
                    )
                )
            }
        }

        val temp5 = _recipes.value ?: mutableListOf()
        val temp6 = temp5.toMutableList()
        temp6.addAll(recipeEntityList)

        _recipes.emit(temp6)
    }

//    suspend fun fetchRecipeIngredients(recipeId: Int) {
//        viewModelScope.launch {
//            try {
//                val response = recipeRepository.getRecipesV3(1, 30, recipeId)
//
//                val ingredientsList = response.row.map {
//
//                }
//            } catch (e: Exception) {
//                // 에러 처리
//            }
//        }
//    }

    suspend fun fetchRecipeFromFirebase(ingredientName: String) = viewModelScope.launch {
        _firebaseRecipes.emit(
            firebaseRepository.getRecipeForTest(ingredientName)
        )
    }

    suspend fun fetchRecipeFromFirebase() = viewModelScope.launch {
        _firebaseRecipes.emit(
            firebaseRepository.getRecipeForTest("")
        )
    }


    fun putRecipeEntity(recipeEntity: RecipeEntity) = viewModelScope.launch(Dispatchers.IO) {
        recipeRepository.putRecipeEntity(recipeEntity)
    }


    private fun encodeToUtf8(query: String): String {
        val convertQuery = convertToOfficial(query)
        return URLEncoder.encode(convertQuery, StandardCharsets.UTF_8.toString())
    }

    suspend fun fetchRecipeIds(ingredientName: String, startIndex: Int, endIndex: Int): List<Int> {
        val ingredientResponse =
            recipeRepository.getRecipeIngredients(startIndex, endIndex, ingredientName)
        return ingredientResponse.row.map { it.recipeId }
    }

    fun getRecipeDetails(
        startIndex: Int,
        endIndex: Int,
        recipeName: String,
        recipeId: Int,
        clientId: String,
        clientSecret: String,
    ) = viewModelScope.launch {
        try {
            val recipes = mutableListOf<RecipeEntity>()
            val recipeDeferred =
                async { recipeRepository.getRecipes(startIndex, endIndex, "", recipeId) }
            val recipeResponse = recipeDeferred.await()
            Log.e("TAG", "getRecipeDetails: recipeDeferred: $recipeResponse")

            val recipe = recipeResponse.row.first()
            val recipeName = recipe.recipeName
            val encodedRecipeName = encodeToUtf8(recipeName)
            val imageDeferred = async {
                naverSearchRepository.searchEncyclopedia(
                    encodedRecipeName,
                    clientId,
                    clientSecret
                )
            }
            val imageResponse = imageDeferred.await()

            val firstItem = imageResponse.items.firstOrNull()
            val thumbnailUrl = firstItem?.thumbnail ?: ""

            val procedureDeferred =
                async { recipeRepository.getRecipeProcedures(startIndex, endIndex, recipeId) }
            val procedureResponse = procedureDeferred.await()
            val procedure = procedureResponse.row.firstOrNull().toString()

            val ingredientDeferred =
                async { recipeRepository.getRecipeIngredients(startIndex, endIndex, recipeId) }
            val ingredientResponse = ingredientDeferred.await()
            val ingredients = ingredientResponse.row.firstOrNull().toString()

            recipes.add(
                RecipeEntity(
                    id = recipe.recipeId,
                    recipeImg = thumbnailUrl,
                    recipeName = recipe.recipeName,
                    explain = recipe.summary,
                    step = procedure,
                    ingredient = ingredients,
                    difficulty = recipe.levelName,
                    time = recipe.cookingTime
                )
            )
            _recipes.emit(recipes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}