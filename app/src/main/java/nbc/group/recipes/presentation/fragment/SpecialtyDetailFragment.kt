package nbc.group.recipes.presentation.fragment

import GridSpacingItemDecoration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.R
import nbc.group.recipes.databinding.FragmentSpecialtyDetailBinding
import nbc.group.recipes.viewmodel.MainViewModel
//import nbc.group.recipes.viewmodel.SpecialtyViewModel

@AndroidEntryPoint
class SpecialtyDetailFragment : Fragment() {

    private var _binding: FragmentSpecialtyDetailBinding? = null
    private val binding: FragmentSpecialtyDetailBinding
        get() = _binding!!
    private var specialtyDetailAdapter: SpecialtyDetailAdapter? = null
//    private val sharedViewModel: SpecialtyViewModel by activityViewModels()
    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecialtyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        specialtyDetailAdapter = SpecialtyDetailAdapter()

        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_effect)
        binding.tvSpecialtyDetail.startAnimation(animation)

        setRecyclerView()
        observeSearchResult()
    }

    private fun setRecyclerView() {
        binding.recyclerViewSpecialtyDetail.apply {
            adapter = specialtyDetailAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            // 아이템 데코레이션
//            addItemDecoration(
//                GridSpacingItemDecoration(
//                    2,
//                    (16 * resources.displayMetrics.density + 0.5f).toInt(),
//                    false
//                )
//            )
        }
    }

    private fun observeSearchResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.searchResult.collect { searchResult ->
                specialtyDetailAdapter?.submitList(searchResult)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        specialtyDetailAdapter = null
    }
}