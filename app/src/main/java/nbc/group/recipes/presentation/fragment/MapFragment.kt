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
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import nbc.group.recipes.BuildConfig.KAKAO_MAP_KEY
import nbc.group.recipes.R
import nbc.group.recipes.data.model.dto.SearchDocumentsResponse
import nbc.group.recipes.databinding.FragmentMapBinding
import nbc.group.recipes.viewmodel.MapViewModel
import java.lang.Exception

@AndroidEntryPoint
class MapFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: FragmentMapBinding? = null

    private lateinit var mapView: MapView
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
        setSearchButton()
        observeViewModel()
    }


    // 지도 띄우는 함수
    private fun showMapView() {

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
        }, object : KakaoMapReadyCallback() {
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


    private fun setSearchButton() {

        binding.searchButton.setOnClickListener {

            val searchText = binding.searchEt.text.toString()

            // 뷰모델에 내가 입력한 텍스트값 전달
            if (searchText.isNotEmpty()) {
                mapViewModel.getregionSearch(searchText)
            } else {
                Snackbar.make(binding.searchEt, "검색어를 입력해주세요", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    // 해당위치로 이동, 라벨 표시
    private fun setMapData(searchDocumentsResponse: SearchDocumentsResponse) {

        // 좌표값 포맷팅
        // y=37.4560044656444, x=126.705258070068 -> 여기서 소숫점 6자리숫자까지만 표시!
        val latitude = searchDocumentsResponse.y
        val longitude = searchDocumentsResponse.x
        val latitude_formatter = String.format("%.6f", latitude).toDouble()  // %.6f는 소수점 이하 6자리까지만 표시
        val longitude_formatter = String.format("%.6f", longitude).toDouble()

        // 이동할 위치(위도,경도) 설정
        val camera = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude_formatter, longitude_formatter))
        // 해당위치로 지도 이동
//        kakaoMap?.moveCamera(camera)
         kakaoMap?.moveCamera(camera, CameraAnimation.from(500,true,true))     // 애니메이션 적용해서 이동


        // 커스텀으로 라벨 생성 및 가져옴
        // 1. LabelStyles 생성 - Icon 이미지 하나만 있는 스타일
        val styles = kakaoMap?.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_map_red)))

        // styles가 null이 아닐때만, LabelOptions 생성하고 라벨추가
        if(styles != null){
            // 2. LabelOptions 생성
            val options = LabelOptions.from(LatLng.from(latitude_formatter, longitude_formatter)).setStyles(styles)

            // 3. LabelLayer 가져옴 (또는 커스텀 Layer 생성)
            val layer = kakaoMap?.labelManager?.getLayer()

            // 4.options 을 넣어 Label 생성
            layer?.addLabel(options)

        }else{
            Log.e("kakaoMap", "LabelStyles null값 에러")
        }

    }


    private fun observeViewModel() {
        mapViewModel.regionSearch.observe(viewLifecycleOwner) {
            Log.d("it_data", it.toString()) // it -> SearchResponse 전체가져옴

            // 검색결과에 documents가 포함되어있으면(무조건 포함함), 그 documents값중 첫번쨰값을 가져옴
            it?.documents?.firstOrNull()?.let { document ->
                Log.d("documents__",document.toString()) // document -> SearchDocumentsResponse의 첫번째값 가져옴
                    setMapData(document)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        kakaoMap = null
    }

}
