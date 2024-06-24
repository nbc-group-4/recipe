package nbc.group.recipes.presentation.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
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
import nbc.group.recipes.viewmodel.SpecialtyViewModel

@AndroidEntryPoint
class SpecialtyFragment : Fragment() {

    private var _binding: FragmentSpecialtyBinding? = null
    private val binding: FragmentSpecialtyBinding
        get() = _binding!!
    private var specialtyAdapter: SpecialtyAdapter? = null
    private val sharedViewModel: SpecialtyViewModel by activityViewModels()
    // private val mainViewModel: MainViewModel by activityViewModels()

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

    private fun setUpListener() = with(binding) {
        etSpecialtySearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                hideKeyboard()
                (activity as MainActivity).moveToSpecialtyDetailFragment()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        backArrow.setOnClickListener {
            (activity as MainActivity).moveToBack()
        }
    }

    private fun performSearch() {
        val searchQuery = binding.etSpecialtySearch.text.toString()
        val selectedKind = binding.tvSpecialtyKind.text.toString()
        val specialties = when (selectedKind) {
            "곡물" -> specialties1
            "채소" -> specialties2
            "과일" -> specialties3
            "어류" -> specialties4
            "고기" -> specialties5
            "기타" -> specialties6
            else -> specialties6
        }
        sharedViewModel.searchItem(searchQuery, specialties)
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
            else -> specialties6
        }
    }

    private fun getImageResourceId(specialty: String): Int {
        return when (specialty) {
            "곡물" -> R.drawable.ic_home_grain
            "채소" -> R.drawable.ic_home_vegetable
            "과일" -> R.drawable.ic_home_fruit
            "어류" -> R.drawable.ic_home_fish
            "고기" -> R.drawable.ic_home_meat
            "기타" -> R.drawable.ic_home_etc
            else -> R.drawable.ic_home_etc
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        specialtyAdapter = null
    }
}