package nbc.group.recipes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.SpecialtyResponse
import nbc.group.recipes.data.model.firebase.UserMetaData
import nbc.group.recipes.data.network.FirebaseResult
import nbc.group.recipes.data.repository.AuthRepository
import nbc.group.recipes.data.repository.FirestoreRepository
import nbc.group.recipes.data.repository.RecipeSpecialtyRepository
import nbc.group.recipes.data.repository.StorageRepository
import nbc.group.recipes.data.utils.getUserProfileStoragePath
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RecipeSpecialtyRepository,
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository,
    private val storageRepository: StorageRepository
): ViewModel() {

    /**
     * <Sample code>
     * private val _recipes = MutableStateFlow(listOf<Recipe>())
     * val recipes = _recipes.asStateFlow()
     *
     * */

    private val _signInFlow = MutableStateFlow<FirebaseResult<FirebaseUser>?>(null)
    val signInFlow = _signInFlow.asStateFlow()

    private val _signUpFlow = MutableStateFlow<FirebaseResult<FirebaseUser>?>(null)
    val signUpFlow = _signUpFlow.asStateFlow()

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user = _user.asStateFlow()
    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    private val _recipes = MutableStateFlow<FirebaseResult<List<Recipe>>?>(null)
    val recipes = _recipes.asStateFlow()

    private val _putRecipeFlow = MutableStateFlow<FirebaseResult<Boolean>?>(null)
    val putRecipeFlow = _putRecipeFlow.asStateFlow()

    private val _userMetaData = MutableStateFlow<FirebaseResult<UserMetaData>?>(null)
    val userMetaData = _userMetaData.asStateFlow()

    init {
        if(authRepository.currentUser != null) {
            viewModelScope.launch {
                _signInFlow.emit(FirebaseResult.Success(authRepository.currentUser!!))
            }
        }
    }

    fun signIn(id: String, pw: String) = viewModelScope.launch {
        val result = authRepository.signIn(id, pw)
        _signInFlow.emit(result)
        _user.emit(authRepository.currentUser)
    }

    fun signUp(name: String, id: String, pw: String) = viewModelScope.launch {
        val result = authRepository.signUp(name, id, pw)
        _signUpFlow.emit(result)
        _user.emit(authRepository.currentUser)
    }

    fun logout() = viewModelScope.launch {
        authRepository.logout()
        _signInFlow.emit(null)
        _signUpFlow.emit(null)
        _user.emit(null)
    }

    fun putRecipe(recipe: Recipe) = viewModelScope.launch {
        val result = firestoreRepository.putRecipe(recipe)
        _putRecipeFlow.emit(result)
    }

    fun getRecipe() = viewModelScope.launch {
        val result = firestoreRepository.getRecipes()
        _recipes.emit(result)
    }

    fun putRecipeTransaction(recipe: Recipe) = viewModelScope.launch {
        currentUser?.let {
            firestoreRepository.putRecipeTransaction(it.uid, recipe)
        }
    }

    fun putUserMeta(
        userMetaData: UserMetaData
    ) = viewModelScope.launch {
        currentUser?.let {
            Log.e("TAG", "putUserMeta: uid: ${it.uid}")
            firestoreRepository.putUserMeta(it.uid, userMetaData)
        }
    }

    fun getUserMeta(uid: String) = viewModelScope.launch {
        val result = firestoreRepository.getUserMeta(uid)
        _userMetaData.emit(result)
    }

    fun putImage(
        inputStream: InputStream
    ) = viewModelScope.launch {
        currentUser?.let {
            storageRepository.putImage(
                getUserProfileStoragePath(it.uid),
                inputStream
            )
        }
    }

    private val _specialties = MutableStateFlow<SpecialtyResponse?>(null)
    val specialties = _specialties.asStateFlow()

    fun doTest(string: String) {
        viewModelScope.launch {
            _specialties.emit(
                repository.getSpecialty("양구")
            )
        }
    }
}
