package nbc.group.recipes.presentation.fragment

import GridSpacingItemDecoration
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import nbc.group.recipes.R
import nbc.group.recipes.databinding.FragmentHomeBinding
import nbc.group.recipes.KindItem
import nbc.group.recipes.dummyRecipe
import nbc.group.recipes.specialtyKind
import nbc.group.recipes.specialtyKindMore
import nbc.group.recipes.viewmodel.SpecialtyViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private var homeQuizAdapter: HomeQuizAdapter? = null
    private var homeKindAdapter: HomeKindAdapter? = null
    private val sharedViewModel: SpecialtyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeQuizAdapter = HomeQuizAdapter()
        homeKindAdapter = HomeKindAdapter(object : HomeKindAdapter.OnItemClickListener {
            override fun onClick(data: KindItem) {
                navigateToSpecialty(data)
                sendSpecialtyKind(data)
            }
        })

        setupRecyclerViewQuiz()
        setupRecyclerViewKind()

        // 더보기 클릭
        binding.btnHomeKindMore.setOnClickListener {
            loadMoreItems()
        }
    }

    private fun setupRecyclerViewQuiz() {
        binding.recyclerViewHomeQuiz.apply {
            adapter = homeQuizAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        homeQuizAdapter?.submitList(dummyRecipe)
    }

    private fun setupRecyclerViewKind() {
        binding.recyclerViewHomeKind.apply {
            adapter = homeKindAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)

        }
        homeKindAdapter?.submitList(specialtyKind)
    }

    private fun loadMoreItems() {
        val currentList = homeKindAdapter?.currentList?.toMutableList()
        if (currentList != null) {
            if (currentList.size == specialtyKind.size) {
                currentList.addAll(specialtyKindMore.take(2))
            }
        }
        homeKindAdapter?.submitList(currentList)
    }

    private fun navigateToSpecialty(item: KindItem) {
        sharedViewModel.setSelectedKindItem(item)
        findNavController().navigate(R.id.specialtyFragment)
        // findNavController().navigate(R.id.action_mainFragment_to_specialtyFragment)
    }

    private fun sendSpecialtyKind(item: KindItem) {
        sharedViewModel.setSelectedKindItem(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        homeQuizAdapter = null
        homeKindAdapter = null
    }
}