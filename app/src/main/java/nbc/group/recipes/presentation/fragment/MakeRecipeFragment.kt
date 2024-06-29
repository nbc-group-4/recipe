package nbc.group.recipes.presentation.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import nbc.group.recipes.data.network.NetworkResult
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

            etRecipeName.addTextChangedListener(textWatcher)
            etRecipeDescription.addTextChangedListener(textWatcher)
            etIngredient.addTextChangedListener(textWatcher)
            etCookingProcess.addTextChangedListener(textWatcher)

            setupSpinner(spinnerCookingTime)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.makeRecipeFlow.collectLatest {
                it?.let { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            (activity as MainActivity).moveToBack()
                        }

                        is NetworkResult.Failure -> {

                        }

                        is NetworkResult.Loading -> {
                            Log.e("MakeRecipeFragment", "onViewCreated: Loading ~")
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
            cookingTime = binding.spinnerCookingTime.selectedItem.toString(),
            typeCode = "user",
            typeName = viewModel.currentUser!!.uid,
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

    private fun setupSpinner(spinner: Spinner) {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cooking_time_options,
            R.layout.spinner_make_recipe
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_drop_down)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateButton()
        }
        override fun afterTextChanged(s: Editable?) {
        }
    }

    private fun updateButton() {
        with(binding) {
            val isRecipeName = etRecipeName.text.toString().isNotEmpty()
            val isRecipeDescription = etRecipeDescription.text.toString().isNotEmpty()
            val isIngredient = etIngredient.text.toString().isNotEmpty()
            val isCookingProcess = etCookingProcess.text.toString().isNotEmpty()

            val allFieldsValid = isRecipeName && isRecipeDescription && isIngredient && isCookingProcess

            if(allFieldsValid){
                btMakeRecipe.isEnabled = true
                btMakeRecipe.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green1))
            }else{
                btMakeRecipe.isEnabled = false
                btMakeRecipe.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.unclick_btn))
            }
        }
    }

}