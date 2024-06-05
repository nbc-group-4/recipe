package nbc.group.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nbc.group.recipes.databinding.ActivityBottomsheetBinding
import nbc.group.recipes.viewmodel.MapViewModel

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {
    private val binding get() = _binding!!
    private var _binding: ActivityBottomsheetBinding? = null

    private val bottomSheetAdapter : BottomSheetAdapter by lazy {
        BottomSheetAdapter{ data, position ->
            // 클릭시 실행할 동작
        }
    }

    private val sharedMapViewModel : MapViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ActivityBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        observeSpecialtyData()
    }


    // 어뎁터와 리사이클러뷰 연결
    private fun setRecyclerView(){

        with(binding.bottomSheetRecyclerView){
            adapter = bottomSheetAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }


    private fun observeSpecialtyData(){
        viewLifecycleOwner.lifecycleScope.launch{

            sharedMapViewModel.specialtie.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collectLatest{
                Log.d("it_region_data__", it.toString())    // "검색한 지역의 특산물"값 받아옴

                // 어뎁터 업데이트
                bottomSheetAdapter.submitList(it?.body?.items?.item)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}