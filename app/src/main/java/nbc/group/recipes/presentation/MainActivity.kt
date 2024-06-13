package nbc.group.recipes.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import nbc.group.recipes.R
import nbc.group.recipes.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController
    }

    fun moveToSignInFragment() = navController
        .navigate(R.id.action_mainFragment_to_signInFragment)
    fun moveToSignUpFragment() = navController
        .navigate(R.id.action_signInFragment_to_signUpFragment)
    fun moveToRecipeFragment() = navController
        .navigate(R.id.action_mainFragment_to_recipeFragment)
    fun moveToRecipeDetailFragment(bundle: Bundle) = navController
        .navigate(R.id.action_recipeFragment_to_recipeDetailFragment, bundle)
    fun moveToSpecialtyFragment() = navController
        .navigate(R.id.action_mainFragment_to_specialtyFragment)
    fun moveToSpecialtyDetailFragment() = navController
        .navigate(R.id.action_specialtyFragment_to_specialtyDetailFragment)
    fun moveToMakeRecipeFragment() = navController
        .navigate(R.id.action_recipeFragment_to_makeRecipeFragment)
    fun moveToBack() = navController.popBackStack()
}
