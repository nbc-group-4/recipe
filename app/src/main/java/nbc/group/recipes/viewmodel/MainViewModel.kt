package nbc.group.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nbc.group.recipes.data.model.dto.SpecialtyResponse
import nbc.group.recipes.data.network.FirebaseResult
import nbc.group.recipes.data.repository.AuthRepository
import nbc.group.recipes.data.repository.RecipeSpecialtyRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RecipeSpecialtyRepository,
    private val authRepository: AuthRepository
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





    private val _specialties = MutableStateFlow<SpecialtyResponse?>(null)
    val specialties = _specialties.asStateFlow()

    fun doTest() {
        viewModelScope.launch {
            _specialties.emit(
                repository.getSpecialty("양구")
            )
        }
    }
}
