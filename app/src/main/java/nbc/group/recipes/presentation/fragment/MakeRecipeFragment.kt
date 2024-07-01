package nbc.group.recipes.presentation.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.network.NetworkResult
import nbc.group.recipes.databinding.FragmentMakeRecipeBinding
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.presentation.adapter.MakeRecipeImageAdapter
import nbc.group.recipes.presentation.adapter.decoration.ListSpacingItemDecoration
import nbc.group.recipes.viewmodel.MainViewModel
import nbc.group.recipes.viewmodel.RecipeGraphViewModel
import java.io.InputStream

@AndroidEntryPoint
class MakeRecipeFragment : Fragment() {

    private var _binding: FragmentMakeRecipeBinding? = null
    private val binding get() = _binding!!

    private var _adapter: MakeRecipeImageAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: MainViewModel by activityViewModels()
    private val recipeGraphViewModel: RecipeGraphViewModel by activityViewModels()

    private val imageUriList = mutableListOf<Uri>()
    private val imageStreamList = mutableListOf<InputStream>()

    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                val inputStream = requireActivity().contentResolver.openInputStream(uri)
                inputStream?.let {
                    imageStreamList.add(it)
                    imageUriList.add(uri)
                    adapter.notifyItemInserted(adapter.itemCount - 1)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMakeRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _adapter = MakeRecipeImageAdapter(recyclerViewItemRemoveButtonClickListener)
        adapter.submitList(imageUriList)

        val itemDecoration = ListSpacingItemDecoration(resources.displayMetrics.density)
        itemDecoration.setPaddingValues(endDp = 12)

        with(binding) {
            ivImageAddButton.setOnClickListener(imageAddButtonClickListener)
            ivBackButton.setOnClickListener(backButtonClickListener)
            btMakeRecipe.setOnClickListener(makeRecipeButtonClickListener)
            rvImages.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            rvImages.addItemDecoration(itemDecoration)
            rvImages.adapter = adapter
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    recipeGraphViewModel.currentSpecialty.collectLatest { nullable ->
                        // todo: 재료 부분 뷰 인에디터블하게 변경 후에 해당 값 삽입 하도록 코드 작성
                        nullable?.let {
                            binding.tvIngredient.text = it.cntntsSj
                        }
                    }
                }

                launch {
                    viewModel.makeRecipeFlow.collectLatest {
                        it?.let { result ->
                            when (result) {
                                is NetworkResult.Success -> {
                                    viewModel.resetMakeRecipeFlow()
                                    (activity as MainActivity).moveToBack()
                                }

                                is NetworkResult.Failure -> {
                                    viewModel.resetMakeRecipeFlow()
                                }

                                is NetworkResult.Loading -> {
                                    Log.e("MakeRecipeFragment", "onViewCreated: Loading ~")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
        imageStreamList.forEach { it.close() }
        imageStreamList.clear()
    }

    private fun currentRecipe(): Recipe {
        val userMeta = viewModel.getUserMeta(viewModel.currentUser!!.uid)
        return Recipe(
            recipeName = binding.etRecipeName.text.toString(),
            summary = binding.etRecipeDescription.text.toString(),
            nationCode = "custom",
            nationName = viewModel.currentUser!!.uid,
            cookingTime = binding.etCookingTime.text.toString(),
            typeCode = "user",
            typeName = viewModel.currentUser!!.uid,
            levelName = "",
            ingredientCode = binding.tvIngredient.text.toString(),
        )
    }


    /**
     * Click Listener
     * */

    private val imageAddButtonClickListener: (View) -> Unit = {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val backButtonClickListener: (View) -> Unit = {
        (activity as MainActivity).moveToBack()
    }

    private val makeRecipeButtonClickListener: (View) -> Unit = {

        // todo: recipe name 에 특수문자 금지

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.putRecipeTransaction(
                recipe = currentRecipe(),
                imageStreamList = imageStreamList
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val recyclerViewItemRemoveButtonClickListener: (Int) -> Unit = {
        Log.e("TAG", "index info: $it")
        imageUriList.removeAt(it)
        imageStreamList[it].close()
        imageStreamList.removeAt(it)
        adapter.notifyDataSetChanged()
    }
}