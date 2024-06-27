package nbc.group.recipes.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem.OnMenuItemClickListener
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nbc.group.recipes.BuildConfig
import nbc.group.recipes.GlideApp
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.databinding.FragmentBottomSheetRecipeDetailBinding
import nbc.group.recipes.databinding.FragmentRecipeDetailBinding
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.viewmodel.MainViewModel
import nbc.group.recipes.viewmodel.MypageSharedViewModel
import nbc.group.recipes.viewmodel.RecipeViewModel


private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class RecipeDetailFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val sharedViewModel: MypageSharedViewModel by activityViewModels() //

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding: FragmentRecipeDetailBinding
        get() = _binding!!

    private var recipeDetail: RecipeEntity? = null
    private val modalBottomSheet = ModalBottomSheet(
        contentBanClickListener = {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                recipeViewModel.insertContentBan(recipeDetail!!.firebaseId)
            }
            (activity as MainActivity).moveToBack()
        },
        userBanClickListener = {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                recipeViewModel.insertUserBan(recipeDetail!!.writerId)
            }
            (activity as MainActivity).moveToBack()
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipeDetail = it.getParcelable("recipeDetail")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (recipeDetail == null) {
            (activity as MainActivity).moveToBack()
        }

        // 마이페이지에서 클릭한 레시피 데이터
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.selectedRecipe.collect { recipeId ->
                recipeId?.let {
                    binding.tvDetailWriter.text = recipeDetail?.writerName
                    binding.tvDetailTitle.text = recipeDetail?.recipeName
                    binding.tvTime.text = recipeDetail?.time
                    binding.tvDetailIngredients.text = recipeDetail?.ingredient
                    binding.tvRecipeSteps.text = recipeDetail?.step
                }
            }
        }

//        recipeDetail?.let { recipe ->
//            recipeViewModel.getRecipeDetails(
//                startIndex = 1,
//                endIndex = 30,
//                recipeName = recipe.recipeName,
//                recipeId =  recipe.id,
//                clientId = BuildConfig.NAVER_CLIENT_ID,
//                clientSecret = BuildConfig.NAVER_CLIENT_SECRET,
//            )
//        }
//
//        observeDifficulty()
        recipeDetail?.let { bindRecipeDetail(it) }
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // 북마크 상태 검사에 따른 UI 업데이트
        // val src = if (북마크 상태 true) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_empty
        binding.ivBookmark.setOnClickListener {
            if (recipeDetail != null) {
                recipeViewModel.putRecipeEntity(recipeDetail!!)
            }
            // setResource(src)
        }
    }

    private fun bindRecipeDetail(recipeDetail: RecipeEntity) {

        with(binding) {
            if (recipeDetail.from == FROM_FIREBASE) {
                GlideApp.with(this@RecipeDetailFragment)
                    .load(
                        Firebase.storage.reference.child(recipeDetail.recipeImg)
                    )
                    .into(ivDetailImg)
            } else {
                Glide.with(requireContext())
                    .load(recipeDetail.recipeImg)
                    .into(ivDetailImg)
            }
            tvDetailWriter.text = if (recipeDetail.from == FROM_FIREBASE) {
                getRecipeDetailWriter(recipeDetail.writerName)
            } else {
                getRecipeDetailWriter(null)
            }
            tvDetailTitle.text = recipeDetail.recipeName
            tvTime.text = recipeDetail.time
            tvDetailIngredients.text = recipeDetail.ingredient
            tvRecipeSteps.text = recipeDetail.step
            ivMoreButton.setOnClickListener {
                modalBottomSheet.show(parentFragmentManager, "")
            }

            if (recipeDetail.from != FROM_FIREBASE) {
                ivMoreButton.visibility = View.GONE
            }


            // todo: Room에 id로 서치하도록 만든 함수를 통해서 북마크 여부 확인
        }
    }

//    private fun observeDifficulty() {
//        lifecycleScope.launchWhenStarted {
//            recipeViewModel.recipes.collect { recipes ->
//                recipes?.let {
//                    val difficulty = it.firstOrNull()?.difficulty
//                    difficulty?.let { setStars(it) }
//                }
//            }
//        }
//    }

    private fun setStars(difficulty: String) {
        when (difficulty) {
            "초보환영" -> {
//                binding.ivStar1.visibility = View.VISIBLE
            }

            "보통" -> {
//                binding.ivStar1.visibility = View.VISIBLE
//                binding.ivStar2.visibility = View.VISIBLE
            }

            "어려움" -> {
//                binding.ivStar1.visibility = View.VISIBLE
//                binding.ivStar2.visibility = View.VISIBLE
//                binding.ivStar3.visibility = View.VISIBLE
            }
        }
    }

    /**
     * todo: 레시피 재료 정보 PI 확인해 볼 것 / 레시피 id를 통해야 전부 받을 수 있다.
     *
     *
     * */


    private fun getRecipeDetailWriter(nickname: String?): String {
        return if (nickname == null) {
            "이 레시피는 공공데이터포털에서 제공합니다."
        } else {
            "이 레시피는 ${nickname}님이 작성하셨습니다."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: RecipeEntity) =
            RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }


    class ModalBottomSheet(
        private val contentBanClickListener: () -> Unit,
        private val userBanClickListener: () -> Unit,
    ) : BottomSheetDialogFragment() {

        private var _bottomSheetBinding: FragmentBottomSheetRecipeDetailBinding? = null
        private val bottomSheetBinding get() = _bottomSheetBinding!!

        private val recipeViewModel: RecipeViewModel by activityViewModels()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _bottomSheetBinding = FragmentBottomSheetRecipeDetailBinding
                .inflate(inflater, container, false)
            return bottomSheetBinding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            with(bottomSheetBinding) {
                tvBanContent.setOnClickListener {
                    contentBanClickListener()
                    dismiss()
                }
                tvBanUser.setOnClickListener {
                    userBanClickListener()
                    dismiss()
                }
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _bottomSheetBinding = null
        }
    }
}