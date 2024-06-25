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
import nbc.group.recipes.StringsSearch
import nbc.group.recipes.StringsSpecialty
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
            StringsSpecialty.KIND1 -> specialties1
            StringsSpecialty.KIND2 -> specialties2
            StringsSpecialty.KIND3 -> specialties3
            StringsSpecialty.KIND4 -> specialties4
            StringsSpecialty.KIND5 -> specialties5
            StringsSpecialty.KIND6 -> specialties6
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
            StringsSpecialty.KIND1 -> specialties1
            StringsSpecialty.KIND2 -> specialties2
            StringsSpecialty.KIND3 -> specialties3
            StringsSpecialty.KIND4 -> specialties4
            StringsSpecialty.KIND5 -> specialties5
            StringsSpecialty.KIND6 -> specialties6
            else -> specialties6
        }
    }

    private fun getImageResourceId(specialty: String): Int {
        return when (specialty) {
            StringsSpecialty.KIND1 -> R.drawable.ic_home_grain
            StringsSpecialty.KIND2 -> R.drawable.ic_home_vegetable
            StringsSpecialty.KIND3 -> R.drawable.ic_home_fruit
            StringsSpecialty.KIND4 -> R.drawable.ic_home_fish
            StringsSpecialty.KIND5 -> R.drawable.ic_home_meat
            StringsSpecialty.KIND6 -> R.drawable.ic_home_etc
            else -> R.drawable.ic_home_etc
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        specialtyAdapter = null
    }
}