package nbc.group.recipes.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.databinding.FragmentRecipeBinding
import nbc.group.recipes.viewmodel.RecipeViewModel

@AndroidEntryPoint
class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding: FragmentRecipeBinding
        get() = _binding!!

    private val viewModel by viewModels<RecipeViewModel>()
    private lateinit var recipeAdapter: RecipeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        fetchRecipes()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter {recipe ->
            // 아이템 클릭 시 로직
        }
        binding.rvRecipe.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = recipeAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.recipes.collect {recipes->
                recipes?.let {
                    recipeAdapter.submitList(it)
                }
            }
        }
    }

    private fun fetchRecipes() {
        viewModel.getRecipes(startIndex = 1, endIndex = 15, recipeName = "", recipeId = 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}