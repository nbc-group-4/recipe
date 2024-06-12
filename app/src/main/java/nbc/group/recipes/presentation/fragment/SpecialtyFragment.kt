package nbc.group.recipes.presentation.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.KindItem
import nbc.group.recipes.R
import nbc.group.recipes.databinding.FragmentSpecialtyBinding
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.presentation.adapter.SpecialtyAdapter
import nbc.group.recipes.specialties1
import nbc.group.recipes.specialties2
import nbc.group.recipes.specialties3
import nbc.group.recipes.specialties4
import nbc.group.recipes.specialties5
import nbc.group.recipes.specialties6
import nbc.group.recipes.specialties7
import nbc.group.recipes.specialties8
import nbc.group.recipes.viewmodel.SpecialtyViewModel

@AndroidEntryPoint
class SpecialtyFragment : Fragment() {

    private var _binding: FragmentSpecialtyBinding? = null
    private val binding: FragmentSpecialtyBinding
        get() = _binding!!
    private var specialtyAdapter: SpecialtyAdapter? = null
    private val sharedViewModel: SpecialtyViewModel by activityViewModels()

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
        setUpListener()
        observeSelectedItem()
    }

    private fun setRecyclerView() {
        binding.recyclerViewSpecialty.apply {
            adapter = specialtyAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun setUpListener() {
        binding.ivBackHome.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 검색창 새로 구현
        binding.etSpecialtySearch.setOnClickListener {
            performSearch()
            hideKeyboard()
            (activity as MainActivity).moveToSpecialtyDetailFragment()
        }
    }

    private fun performSearch() {
        val searchQuery = binding.etSpecialtySearch.text.toString()
        sharedViewModel.searchItem(searchQuery)
    }

    private fun hideKeyboard() {
        val imm =
            view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    // 홈 프래그먼트에서 클릭시 특산물 종류 수신
    private fun observeSelectedItem() {
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.selectedKindItem.collect { item ->
                item?.let {
                    binding.tvSpecialtyKind.text = item.item

                    val specialtyItems = displaySpecialtyItems(it.item)
                    val kindItemList = specialtyItems.map { specialty ->
                        KindItem(specialty, getImageResourceId(specialty))
                    }
                    specialtyAdapter?.submitList(kindItemList)
                }
            }
        }
    }

    private fun displaySpecialtyItems(kind: String): List<String> {
        return when (kind) {
            "곡물" -> specialties1
            "채소" -> specialties2
            "과일" -> specialties3
            "어류" -> specialties4
            "고기" -> specialties5
            "기타" -> specialties6
            "쌀" -> specialties7
            "해조류" -> specialties8
            else -> specialties6
        }
    }

    private fun getImageResourceId(specialty: String): Int {
        return when (specialty) {
            "곡물" -> R.drawable.img_specialty_kind1
            "채소" -> R.drawable.img_specialty_kind2
            "과일" -> R.drawable.img_specialty_kind3
            "어류" -> R.drawable.img_specialty_kind4
            "고기" -> R.drawable.img_specialty_kind5
            "기타" -> R.drawable.img_specialty_kind6
            "쌀" -> R.drawable.img_specialty_kind7
            "해조류" -> R.drawable.img_specialty_kind8
            else -> R.drawable.img_specialty_kind6
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        specialtyAdapter = null
    }
}
