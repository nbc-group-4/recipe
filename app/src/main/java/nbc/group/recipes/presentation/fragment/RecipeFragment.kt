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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import nbc.group.recipes.BuildConfig
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

    private val viewModel by viewModels<RecipeViewModel>()
    private val mapSharedViewModel : MapSharedViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var recipeAdapter: RecipeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // version yegin
        setupRecyclerView()
//        observeViewModel()
//        observeSharedViewModel()
        mainViewModel.resetMakeRecipeFlow()

        // version jun
        initializerVersionRalph()
        viewModelObserverVersionRalph()
    }

//    private fun setSpinner() {
//        binding.spRecipeType.onItemClickListener = object : AdapterView.OnItemClickListener {
//            override fun onItemClick(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//
//            }
//
//            fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//        }
//    }
//
//    private fun filterNation(selectedNation: String?) {
//
//    }

    private fun viewModelObserverVersionRalph() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.testRecipe.collect {
                it?.let { recipes ->
                    recipes.forEach { recipe ->
                        Log.e("URGENT_TAG", "viewModelObserverVersionRalph: ${recipe.step}", )
                    }
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
                        viewModel.fetchRecipeIdsV2(official)
                    } catch(e: Exception) {
                        (activity as MainActivity).moveToBack()
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter { recipe ->
            val bundle = Bundle().apply { putParcelable("recipeDetail", recipe) }
            (activity as MainActivity).moveToRecipeDetailFragment(bundle)
        }
        binding.rvRecipe.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = recipeAdapter
            setHasFixedSize(true)
        }
        binding.btBack.setOnClickListener {
            (activity as MainActivity).moveToBack()
        }

        binding.btMakeRecipe.setOnClickListener {
            (activity as MainActivity).moveToMakeRecipeFragment()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.recipes.collect { recipes ->
                recipes?.let {
                    Log.e("TAG", "observeViewModel: $it", )
                    recipeAdapter.submitList(it)
                }
            }
        }
    }

    private fun observeSharedViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            mapSharedViewModel.selectedSpecialty.collect {specialty ->

                Log.d(
                    "observeSharedViewModel___",
                    specialty.toString()
                )   // it = 클릭한 특산물데이터에 대한 정보 들어옴

                val startIndex = 1
                val endIndex = 30
                val clientId = BuildConfig.NAVER_CLIENT_ID
                val clientSecret = BuildConfig.NAVER_CLIENT_SECRET

                val officialSpecialty = specialty?.cntntsSj?.let {
                    try {
                        Log.e("TAG", "observeSharedViewModel: $it")
                        convertToOfficial(it)
                    } catch(e: Exception) {
                        ""
                    }
                }

                officialSpecialty?.let { ingredientName ->
                    Log.e("URGENT_TAG", "observeSharedViewModel: 1: $ingredientName", )
                    val recipeIds = viewLifecycleOwner.lifecycleScope.async {
                        viewModel.fetchRecipeIds(ingredientName, startIndex, endIndex)
                    }.await()
                    Log.e("URGENT_TAG", "observeSharedViewModel: 2", )
                    recipeIds.forEach { recipeId ->
                        viewModel.getRecipeDetails(
                            startIndex,
                            endIndex,
                            ingredientName,
                            recipeId,
                            clientId,
                            clientSecret
                        )
                    }
                }

                binding.tvSpcialityName.text = officialSpecialty
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}