package nbc.group.recipes.presentation.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.R
import nbc.group.recipes.presentation.adapter.BookMarkAdapter
import nbc.group.recipes.viewmodel.BookMarkViewModel
import nbc.group.recipes.VisibilityView
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.model.dto.toRecipe
import nbc.group.recipes.data.model.entity.RecipeEntity
import nbc.group.recipes.databinding.CustomDialogBinding
import nbc.group.recipes.databinding.FragmentBookmarkBinding
import nbc.group.recipes.presentation.MainActivity

@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: FragmentBookmarkBinding? = null

    private val bookMarkViewModel : BookMarkViewModel by viewModels()

    private lateinit var recipeEntity : RecipeEntity

    private val bookMarkAdapter : BookMarkAdapter by lazy {
        BookMarkAdapter(
            onClick = { item, position ->
                recipeEntity = item
                val recipeEntity = RecipeEntity(
                    id = recipeEntity.id,
                    recipeImg = recipeEntity.recipeImg,
                    recipeName = recipeEntity.recipeName,
                    explain = recipeEntity.explain,
                    step = "",
                    ingredient = recipeEntity.ingredient,
                    difficulty = recipeEntity.step,
                    time = recipeEntity.time
                )
                navigateToRecipeDetail(recipeEntity)
            },
            onLongClick = { item, position ->
                recipeEntity = item

                CustomDialog(recipeEntity =
                RecipeEntity(
                    id = recipeEntity.id,
                    recipeImg = recipeEntity.recipeImg,
                    recipeName = recipeEntity.recipeName,
                    explain = recipeEntity.explain,
                    step = "",
                    ingredient = recipeEntity.ingredient,
                    difficulty = recipeEntity.step,
                    time = recipeEntity.time
                ))
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setObserve()
    }


    private fun setRecyclerView(){
        with(binding.recyclerView){
            adapter = bookMarkAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }


    // AllData 관찰하고 UI업데이트
    private fun setObserve(){
        viewLifecycleOwner.lifecycleScope.launch {
            bookMarkViewModel.recipeEntity.collect { recipeEntityList ->

                val recipeList = recipeEntityList.map {
                    it.toRecipe()
                }
                bookMarkAdapter.submitList(recipeEntityList)
                bookMarkViewModel.checkVisiblityView()

                binding.tvBookmarkCount.text = "총 ${recipeEntityList.size}개"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            bookMarkViewModel.visibilityView.collect{
                when(it){
                    VisibilityView.EMPTYVIEW -> {
                        with(binding){
                            tvEmpty.visibility = View.VISIBLE
                            ivBookmarkEmpty.visibility = View.VISIBLE
                            recyclerView.visibility = View.INVISIBLE
                        }
                    }
                    VisibilityView.RECYCLERVIEW -> {
                        with(binding){
                            tvEmpty.visibility = View.INVISIBLE
                            ivBookmarkEmpty.visibility = View.INVISIBLE
                            recyclerView.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }


    private fun CustomDialog(recipeEntity: RecipeEntity){

        val dialog = Dialog(requireContext())
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.dialogCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.dialogDeleteBtn.setOnClickListener {
            bookMarkViewModel.deleteData(recipeEntity)
            Toast.makeText(requireContext(), "삭제완료", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun navigateToRecipeDetail(recipeEntity: RecipeEntity) {
        val bundle = Bundle().apply {
            putParcelable("recipeDetail", recipeEntity)
        }
        (activity as? MainActivity)?.moveToRecipeDetailFragment(bundle)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}