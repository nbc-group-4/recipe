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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nbc.group.recipes.R
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.network.FirebaseResult
import nbc.group.recipes.databinding.FragmentMakeRecipeBinding
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.presentation.adapter.MakeRecipeImageAdapter
import nbc.group.recipes.presentation.adapter.decoration.ListSpacingItemDecoration
import nbc.group.recipes.viewmodel.MainViewModel
import java.io.InputStream

@AndroidEntryPoint
class MakeRecipeFragment : Fragment() {

    private var _binding: FragmentMakeRecipeBinding? = null
    private val binding get() = _binding!!

    private var _adapter: MakeRecipeImageAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: MainViewModel by activityViewModels()

    private val imageUriList = mutableListOf<Uri>()
    private val imageStreamList = mutableListOf<InputStream>()

    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if(uri != null) {
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.makeRecipeFlow.collectLatest {
                it?.let { result ->
                    when(result) {
                        is FirebaseResult.Success -> {
                            (activity as MainActivity).moveToBack()
                        }
                        is FirebaseResult.Failure -> {

                        }
                        is FirebaseResult.Loading -> {
                            Log.e("MakeRecipeFragment", "onViewCreated: Loading ~", )
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
        return Recipe(
            recipeName = binding.etRecipeName.text.toString(),
            summary = binding.etRecipeDescription.text.toString(),
            nationCode = "custom",
            nationName = viewModel.currentUser!!.uid,
            cookingTime = binding.etCookingTime.text.toString(),
            levelName = "",
            ingredientCode = binding.etIngredient.text.toString(),
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.putRecipeTransaction(
                recipe = currentRecipe(),
                imageStreamList = imageStreamList
            )

            imageStreamList.forEach { it.close() }
            imageStreamList.clear()
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