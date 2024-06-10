package nbc.group.recipes.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import nbc.group.recipes.BuildConfig
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.databinding.FragmentRecipeDetailBinding
import nbc.group.recipes.viewmodel.RecipeViewModel

@AndroidEntryPoint
class RecipeDetailFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by viewModels()
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding: FragmentRecipeDetailBinding
        get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeDetail = arguments?.getParcelable<RecipeEntity>("recipeDetail")

        recipeDetail?.let { recipe ->
            recipeViewModel.getRecipeDetails(
                startIndex = 1,
                endIndex = 30,
                recipeName = recipe.recipeName,
                recipeId =  recipe.id,
                clientId = BuildConfig.NAVER_CLIENT_ID,
                clientSecret = BuildConfig.NAVER_CLIENT_SECRET,
            )
        }

        observeDifficulty()
        recipeDetail?.let { bindRecipeDetail(it) }
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // val src = if (북마크 상태 true) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_empty
        binding.ivBookmark.setOnClickListener {
            // setResource(src)
        }
    }

    private fun bindRecipeDetail(recipeDetail: RecipeEntity) {
        Glide.with(requireContext())
            .load(recipeDetail.recipeImg)
            .into(binding.ivDetailImg)

        binding.tvDetailTitle.text = recipeDetail.recipeName
        binding.tvTime.text = recipeDetail.time
        binding.tvDetailIngredients.text = recipeDetail.ingredient
        binding.tvRecipeSteps.text = recipeDetail.step
    }

    private fun observeDifficulty() {
        lifecycleScope.launchWhenStarted {
            recipeViewModel.recipes.collect { recipes ->
                recipes?.let {
                    val difficulty = it.firstOrNull()?.difficulty
                    difficulty?.let { setStars(it) }
                }
            }
        }
    }

    private fun setStars(difficulty: String) {
        when(difficulty) {
            "초보환영" -> {
                binding.ivStar1.visibility = View.VISIBLE
            }
            "보통" -> {
                binding.ivStar1.visibility = View.VISIBLE
                binding.ivStar2.visibility = View.VISIBLE
            }
            "어려움" -> {
                binding.ivStar1.visibility = View.VISIBLE
                binding.ivStar2.visibility = View.VISIBLE
                binding.ivStar3.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}