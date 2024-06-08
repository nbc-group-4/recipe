package nbc.group.recipes.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.BookMarkAdapter
import nbc.group.recipes.BookMarkViewModel
import nbc.group.recipes.VisibilityView
import nbc.group.recipes.data.model.dto.toRecipe
import nbc.group.recipes.databinding.FragmentBookmarkBinding

@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: FragmentBookmarkBinding? = null

    private val bookMarkViewModel : BookMarkViewModel by viewModels()

    private val bookMarkAdapter : BookMarkAdapter by lazy {
        BookMarkAdapter(
            onClick = { item, position ->
                // 클릭시

            },
            onLongClick = { item, position ->
                // 롱클릭시

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
                bookMarkAdapter.submitList(recipeList)

                bookMarkViewModel.checkStorageEntities()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            bookMarkViewModel.visibilityView.collect{
                when(it){
                    VisibilityView.EMPTYVIEW -> {
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.INVISIBLE
                    }
                    VisibilityView.RECYCLERVIEW -> {
                        binding.tvEmpty.visibility = View.INVISIBLE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}