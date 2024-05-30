package nbc.group.recipes.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import dagger.hilt.android.AndroidEntryPoint
import nbc.group.recipes.BuildConfig.KAKAO_MAP_KEY
import nbc.group.recipes.databinding.FragmentMapBinding
import nbc.group.recipes.viewmodel.MapViewModel
import java.lang.Exception

@AndroidEntryPoint
class MapFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding : FragmentMapBinding? = null

    private lateinit var mapView : MapView
    private var kakaoMap : KakaoMap? = null

    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showMapView()
        searchApiData()
    }



    private fun showMapView(){

        mapView = binding.mapView

        // KakaoMapSDK 초기화!!
        KakaoMapSdk.init(requireContext(), KAKAO_MAP_KEY)

        mapView.start(object : MapLifeCycleCallback() {

            override fun onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출
                Log.d("KakaoMap", "onMapDestroy")
            }

            override fun onMapError(p0: Exception?) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출
                Log.e("KakaoMap", "onMapError")
            }
        }, object : KakaoMapReadyCallback(){
                override fun onMapReady(kakaomap: KakaoMap) {
                    // 정상적으로 인증이 완료되었을 때 호출
                    kakaoMap = kakaomap
                }

            override fun getZoomLevel(): Int {
                // 지도 시작 시 확대/축소 줌 레벨 설정 (8~10사이가 적당)
                return 8
            }
        })
    }


    private fun searchApiData(){

        binding.searchButton.setOnClickListener {
            val searchText = binding.searchEt.text.toString()

            // 뷰모델에 내가 입력한 텍스트값 전달
            if (searchText.isNotEmpty()){
                mapViewModel.getregionSearch(searchText)
            }else{
                Snackbar.make(binding.searchEt, "검색어를 입력해주세요", Snackbar.LENGTH_SHORT).show()
            }
        }

        mapViewModel.regionSearch.observe(viewLifecycleOwner){
            Log.d("it_searchData", it.toString())    // 전체데이터(검색버튼 눌렀을때 가져오는 전체데이터)
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}