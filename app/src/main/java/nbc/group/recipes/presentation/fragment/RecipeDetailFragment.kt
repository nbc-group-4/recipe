package nbc.group.recipes.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import nbc.group.recipes.BuildConfig
import nbc.group.recipes.data.model.dto.RecipeIngredient
import nbc.group.recipes.data.model.dto.RecipeProcedure
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
        val recipeDetail = arguments?.getParcelable<RecipeEntity>("recipeDetail")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setStars() <- 난이도(LEVEL_NM) 값을 String 넘겨 줘야 함

        recipeViewModel.getRecipeDetails(startIndex = 1, endIndex = 15, recipeName = "약식", recipeId = 5, clientId = BuildConfig.NAVER_CLIENT_ID, clientSecret = BuildConfig.NAVER_CLIENT_SECRET)
//        recipeViewModel.recipeIngredients.observe(viewLifecycleOwner) { ingredients ->
//            binding.tvDetailIngredients.text = ingredientsInfo()
//        }

//        recipeViewModel.recipeProcedures.observe(viewLifecycleOwner) { procedures ->
//            binding.tvDetailSteps.text = proceduresInfo(procedures)
//        }

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

//    private fun ingredientsInfo(ingredients: List<RecipeIngredient>?): String {
//        // 재료 정보 데이터 처리 및 반한
//    }

//    private fun proceduresInfo(procedures: List<RecipeProcedure>?): String {
//        // 과정 정보 데이터 처리 및 반환
//    }

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