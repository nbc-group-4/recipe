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
import kotlinx.coroutines.launch
import nbc.group.recipes.BuildConfig
import nbc.group.recipes.R
import nbc.group.recipes.databinding.FragmentRecipeBinding
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.viewmodel.MapViewModel
import nbc.group.recipes.viewmodel.RecipeViewModel
@AndroidEntryPoint
class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding: FragmentRecipeBinding
        get() = _binding!!

    private val viewModel by viewModels<RecipeViewModel>()
    private val sharedMapViewModel : MapViewModel by activityViewModels()
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
//        fetchRecipes()
        observeSharedViewModel()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter { recipe ->
            val bundle = Bundle().apply { putParcelable("recipeDetail", recipe) }
            (activity as MainActivity).moveToRecipeDetailFragment(bundle)


//            val detailFragment = RecipeDetailFragment()
//            detailFragment.arguments = bundle
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(androidx.fragment.R.id.fragment_container_view_tag, detailFragment)
//                .addToBackStack(null)
//                .commit()
        }
        binding.rvRecipe.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = recipeAdapter
            setHasFixedSize(true)
        }
        binding.btBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.recipes.collect { recipes ->
                recipes?.let {
                    recipeAdapter.submitList(it)
                }
            }
        }
    }

    private fun observeSharedViewModel(){
        viewLifecycleOwner.lifecycleScope.launch {
            sharedMapViewModel.selectedSpecialty.collect{
                Log.d("observeSharedViewModel___" , it.toString())   // it = 클릭한 특산물데이터에 대한 정보 들어옴
            }
        }
    }


    private fun fetchRecipes() {
        viewModel.getRecipeDetails(
            startIndex = 1,
            endIndex = 15,
            recipeName = "",
            recipeId = 0,
            clientId = BuildConfig.NAVER_CLIENT_ID,
            clientSecret = BuildConfig.NAVER_CLIENT_SECRET
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}