package nbc.group.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nbc.group.recipes.databinding.ActivityBottomsheetBinding


class BottomSheetFragment : BottomSheetDialogFragment() {

    private val binding get() = _binding!!
    private var _binding: ActivityBottomsheetBinding? = null

    private val bottomSheetAdapter : BottomSheetAdapter by lazy {
        BottomSheetAdapter{ data, position ->
            // 클릭시 실행할 동작
        }
    }


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
        TestData()
    }


    // 어뎁터와 리사이클러뷰 연결
    private fun setRecyclerView(){

        with(binding.bottomSheetRecyclerView){
            adapter = bottomSheetAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }


    private fun TestData(){
        val testDatas = listOf(
            TestData(1,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQOByQi_wqBIRiSI8ta4O05kp-awGDIlYVhHQ&s", "title1", "description1"),
            TestData(2,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQOByQi_wqBIRiSI8ta4O05kp-awGDIlYVhHQ&s", "title2", "description2"),
            TestData(3,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQOByQi_wqBIRiSI8ta4O05kp-awGDIlYVhHQ&s", "title3", "description3"),
            TestData(4,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz5JQWrFmc7emFhAXlVIKsjZmTaromlosdyA&s", "title4", "description4"),
            TestData(5,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz5JQWrFmc7emFhAXlVIKsjZmTaromlosdyA&s", "title5", "description5"),
            TestData(6,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz5JQWrFmc7emFhAXlVIKsjZmTaromlosdyA&s", "title6", "description6"),
            TestData(7,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz5JQWrFmc7emFhAXlVIKsjZmTaromlosdyA&s", "title7", "description7"),
            TestData(8,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz5JQWrFmc7emFhAXlVIKsjZmTaromlosdyA&s", "title8", "description8"),
            TestData(9,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz5JQWrFmc7emFhAXlVIKsjZmTaromlosdyA&s", "title8", "description8"),
            TestData(10,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz5JQWrFmc7emFhAXlVIKsjZmTaromlosdyA&s", "title8", "description8"),
            TestData(11,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz5JQWrFmc7emFhAXlVIKsjZmTaromlosdyA&s", "title8", "description8"),
            TestData(12,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQz5JQWrFmc7emFhAXlVIKsjZmTaromlosdyA&s", "title8", "description8"),
        )

        bottomSheetAdapter.submitList(testDatas)
        Log.d("testData",testDatas.toString())
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}