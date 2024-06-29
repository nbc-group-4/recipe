package nbc.group.recipes.presentation.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nbc.group.recipes.convertToOfficial
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.data.network.NetworkResult
import nbc.group.recipes.data.utils.getRecipeStoragePath
import nbc.group.recipes.databinding.FragmentRecipeBinding
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.presentation.adapter.RecipeAdapter
import nbc.group.recipes.presentation.adapter.decoration.GridSpacingItemDecoration
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

    private val recipeViewModel: RecipeViewModel by activityViewModels()
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
        // initChipGroup()

        initializer()
        viewModelObserverVersionRalph()
    }

    private fun viewModelObserverVersionRalph() {
        viewLifecycleOwner.lifecycleScope.launch {
            recipeViewModel.recipes.collectLatest {
                it?.let { recipes ->
                    Log.e("URGENT_TAG", "viewModelObserverVersionRalph: $recipes", )
                    recipeAdapter.submitList(recipes)
                    recipeAdapter.notifyDataSetChanged()
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            recipeViewModel.firebaseRecipes.collect { nullable ->
                Log.e("URGENT_TAG", "viewModelObserverVersionRalph: $nullable", )
                nullable?.let { nonNull ->
                    when(nonNull) {
                        is NetworkResult.Success -> {
                            val temp = recipeAdapter.currentList.toMutableList()
                            nonNull.result.forEach {
                                val temp1 = it.recipeName.split("/")
                                Log.e("URGENT_TAG", "viewModelObserverVersionRalph: $it", )
                                val id = temp1[0]
                                val name = temp1[1]
                                temp.add(
                                    RecipeEntity(
                                        id = it.recipeId,
                                        recipeImg = getRecipeStoragePath(id),
                                        recipeName = name,
                                        explain = it.summary,
                                        step = it.summary,
                                        ingredient = it.ingredientCode,
                                        difficulty = it.levelName,
                                        time = it.cookingTime,
                                        from = FROM_FIREBASE
                                    )
                                )
                            }
                            recipeViewModel.mergeFirebaseData(temp)
                            mapSharedViewModel.selectedSpecialty.value?.let {
                                recipeViewModel.fetchRecipe(it.cntntsSj!!)
                            }
                        }
                        is NetworkResult.Failure -> {
                            Log.e("URGENT_TAG", "viewModelObserverVersionRalph: failure", )
                        }
                        is NetworkResult.Loading -> {
                            Log.e("URGENT_TAG", "viewModelObserverVersionRalph: loading", )
                        }
                    }
                }
            }
        }
    }

    private fun initializer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mapSharedViewModel.selectedSpecialty.collect { specialty ->
                    try {
                        val official = convertToOfficial(specialty!!.cntntsSj!!)
                        recipeViewModel.fetchRecipeFromFirebase(official)
                    } catch(e: Exception) {
                        // 예상 에러 -> official 변경 불가 & specilay == null
                        Log.e("URGENT_TAG", "initializer: exception", )
                        recipeViewModel.fetchRecipeFromFirebase()
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
            addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    (20 * resources.displayMetrics.density + 0.5f).toInt(),
                    false
                )
            )
            adapter = recipeAdapter
            setHasFixedSize(true)
        }

        binding.btMakeRecipe.setOnClickListener(makeButtonClickListener)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _recipeAdapter = null
        recipeViewModel.clearRecipes()
    }

    private val makeButtonClickListener: (View) -> Unit = {
        if(mainViewModel.currentUser == null) {
            showDialog()
        } else {
            (activity as MainActivity).moveToMakeRecipeFragment()
        }
    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(requireActivity())
            .setTitle("로그인이 필요한 서비스입니다.")
            .setPositiveButton("확인") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        dialog.show()
    }
}