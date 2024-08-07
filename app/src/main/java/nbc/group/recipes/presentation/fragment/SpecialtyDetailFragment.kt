package nbc.group.recipes.presentation.fragment

import nbc.group.recipes.presentation.adapter.decoration.GridSpacingItemDecoration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.data.model.dto.Item
import nbc.group.recipes.databinding.FragmentSpecialtyDetailBinding
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.presentation.adapter.SpecialtyDetailAdapter
import nbc.group.recipes.viewmodel.SpecialtyViewModel

@AndroidEntryPoint
class SpecialtyDetailFragment : Fragment() {

    private var _binding: FragmentSpecialtyDetailBinding? = null
    private val binding: FragmentSpecialtyDetailBinding
        get() = _binding!!
    private var specialtyDetailAdapter: SpecialtyDetailAdapter? = null
    private val sharedViewModel: SpecialtyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecialtyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        specialtyDetailAdapter = SpecialtyDetailAdapter { item ->
            onItemClicked(item)
        }

        setRecyclerView()
        setUpListener()
        observeSearchResult()
    }

    private fun setRecyclerView() {
        binding.recyclerViewSpecialtyDetail.apply {
            adapter = specialtyDetailAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)

            addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    (16 * resources.displayMetrics.density + 0.5f).toInt(),
                    false
                )
            )
        }
    }

    private fun onItemClicked(item: Item) {

    }

    private fun setUpListener() = with(binding) {
        backArrow.setOnClickListener {
            (activity as MainActivity).moveToBack()
        }
    }

    private fun observeSearchResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.searchResult.collect { searchResult ->
                if (searchResult.isEmpty()) {
                    binding.recyclerViewSpecialtyDetail.visibility = View.GONE
                    binding.ivSpecialtyDetailEmpty.visibility = View.VISIBLE
                    binding.tvSpecialtyDetailEmpty.visibility = View.VISIBLE
                } else {
                    binding.recyclerViewSpecialtyDetail.visibility = View.VISIBLE
                    binding.ivSpecialtyDetailEmpty.visibility = View.GONE
                    binding.tvSpecialtyDetailEmpty.visibility = View.GONE
                    specialtyDetailAdapter?.submitList(searchResult)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        specialtyDetailAdapter = null
    }
}
