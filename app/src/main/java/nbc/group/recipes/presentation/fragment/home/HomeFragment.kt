package nbc.group.recipes.presentation.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import nbc.group.recipes.R
import nbc.group.recipes.databinding.FragmentHomeBinding
import nbc.group.recipes.util.KindItem
import nbc.group.recipes.util.dummyRecipe
import nbc.group.recipes.util.specialtyKind
import nbc.group.recipes.util.specialtyKindMore
import nbc.group.recipes.viewmodel.MainViewModel
import nbc.group.recipes.viewmodel.SharedViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private var homeQuizAdapter: HomeQuizAdapter? = null
    private var homeKindAdapter: HomeKindAdapter? = null
    private val mainViewModel: MainViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

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
            }
        })

        setupRecyclerViewQuiz()
        setupRecyclerViewKind()

        // 더보기 클릭
        binding.btnHomeKindMore.setOnClickListener {
            loadMoreItems()
        }

        // 툴바의 검색 아이콘 클릭 - 특산물 검색?
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.mapFragment) // 검색 결과 화면?
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

    // 특산품 프래그먼트로 이동
    private fun navigateToSpecialty(item: KindItem) {
        sharedViewModel.setSelectedKindItem(item)
        findNavController().navigate(R.id.specialtyFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        homeQuizAdapter = null
        homeKindAdapter = null
    }
}