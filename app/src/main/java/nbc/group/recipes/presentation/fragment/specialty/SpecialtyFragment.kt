package nbc.group.recipes.presentation.fragment.specialty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.databinding.FragmentSpecialtyBinding
import nbc.group.recipes.viewmodel.MainViewModel
import nbc.group.recipes.viewmodel.SharedViewModel

@AndroidEntryPoint
class SpecialtyFragment : Fragment() {

    private var _binding: FragmentSpecialtyBinding? = null
    private val binding: FragmentSpecialtyBinding
        get() = _binding!!
    private var specialtyAdapter: SpecialtyAdapter? = null
    private val mainViewModel: MainViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecialtyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        specialtyAdapter = SpecialtyAdapter()

        setRecyclerView()

        // 특산품 종류별로 구분 - 작업 중
        val specialtyType = arguments?.getString("specialtyType") ?: ""
        mainViewModel.getSpecialties(specialtyType)
        observeViewModel()

        // API 테스트
        binding.tvSpecialtyKind.setOnClickListener {
            fetchSpecialtyData("부산")
        }

        observeSelectedItem()
    }

    // 각 특산품 리사이클러뷰
    private fun setRecyclerView() {
        binding.recyclerViewSpecialtyDetail.apply {
            binding.recyclerViewSpecialtyDetail.adapter = adapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    // 특산품 API 뷰모델
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.specialties.collect { list ->
                list?.let {
                    specialtyAdapter?.submitList(it)
                }
            }
        }
    }

    // API 테스트
    private fun fetchSpecialtyData(specialtyType: String) {
        mainViewModel.doTest(specialtyType)
    }

    // 홈 프래그먼트에서 클릭시 특산품 종류 수신
    private fun observeSelectedItem() {
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.selectedKindItem.collect { item ->
                item?.let {
                    binding.tvSpecialtyKind.text = it.item
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        specialtyAdapter = null
    }
}