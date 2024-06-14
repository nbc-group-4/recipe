package nbc.group.recipes.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.convertToOfficial
import nbc.group.recipes.databinding.FragmentRecipeBinding
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.presentation.adapter.RecipeAdapter
import nbc.group.recipes.viewmodel.MainViewModel
import nbc.group.recipes.viewmodel.MapSharedViewModel
import nbc.group.recipes.viewmodel.RecipeViewModel

@AndroidEntryPoint
class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding: FragmentRecipeBinding
        get() = _binding!!

    private var _recipeAdapter: RecipeAdapter? = null
    private val recipeAdapter
        get() = _recipeAdapter!!

    private val recipeViewModel: RecipeViewModel by viewModels()
    private val mapSharedViewModel : MapSharedViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        mainViewModel.resetMakeRecipeFlow()
        initializerVersionRalph()
        viewModelObserverVersionRalph()
        initChipGroup()
    }

    private fun viewModelObserverVersionRalph() {
        viewLifecycleOwner.lifecycleScope.launch {
            recipeViewModel.recipes.collect {
                it?.let { recipes ->
                    recipeAdapter.submitList(recipes)
                }
            }
        }
    }

    private fun initializerVersionRalph() {
        viewLifecycleOwner.lifecycleScope.launch {
            mapSharedViewModel.selectedSpecialty.collect { nullable ->
                nullable?.let { specialty ->
                    try {
                        val official = convertToOfficial(specialty.cntntsSj!!)
                        recipeViewModel.fetchRecipe(official)
                    } catch(e: Exception) {
                        (activity as MainActivity).moveToBack()
                    }
                }
            }
        }
    }

    private fun initChipGroup() {
        RECIPE_NATION_LIST.forEach {
            binding.cgRecipeNation.addView(
                Chip(activity).apply {
                    text = it
                    isCheckable = true
                }
            )
        }
    }

    private fun setupRecyclerView() {
        _recipeAdapter = RecipeAdapter(this@RecipeFragment) { recipe ->
            val bundle = Bundle().apply { putParcelable("recipeDetail", recipe) }
            (activity as MainActivity).moveToRecipeDetailFragment(bundle)
        }
        binding.rvRecipe.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = recipeAdapter
            setHasFixedSize(true)
        }

        binding.btMakeRecipe.setOnClickListener {
            (activity as MainActivity).moveToMakeRecipeFragment()
        }
    }


//
//    private fun observeViewModel() {
//        lifecycleScope.launch {
//            viewModel.recipes.collect { recipes ->
//                recipes?.let {
//                    Log.e("TAG", "observeViewModel: $it", )
//                    recipeAdapter.submitList(it)
//                }
//            }
//        }
//    }
//
//    private fun observeSharedViewModel() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            mapSharedViewModel.selectedSpecialty.collect {specialty ->
//
//                Log.d(
//                    "observeSharedViewModel___",
//                    specialty.toString()
//                )   // it = 클릭한 특산물데이터에 대한 정보 들어옴
//
//                val startIndex = 1
//                val endIndex = 30
//                val clientId = BuildConfig.NAVER_CLIENT_ID
//                val clientSecret = BuildConfig.NAVER_CLIENT_SECRET
//
//                val officialSpecialty = specialty?.cntntsSj?.let {
//                    try {
//                        Log.e("TAG", "observeSharedViewModel: $it")
//                        convertToOfficial(it)
//                    } catch(e: Exception) {
//                        ""
//                    }
//                }
//
//                officialSpecialty?.let { ingredientName ->
//                    Log.e("URGENT_TAG", "observeSharedViewModel: 1: $ingredientName", )
//                    val recipeIds = viewLifecycleOwner.lifecycleScope.async {
//                        viewModel.fetchRecipeIds(ingredientName, startIndex, endIndex)
//                    }.await()
//                    Log.e("URGENT_TAG", "observeSharedViewModel: 2", )
//                    recipeIds.forEach { recipeId ->
//                        viewModel.getRecipeDetails(
//                            startIndex,
//                            endIndex,
//                            ingredientName,
//                            recipeId,
//                            clientId,
//                            clientSecret
//                        )
//                    }
//                }
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _recipeAdapter = null
    }
}