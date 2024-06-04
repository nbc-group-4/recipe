package nbc.group.recipes.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.State.*
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayerOptions
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTransition
import com.kakao.vectormap.label.Transition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nbc.group.recipes.BottomSheetAdapter
import nbc.group.recipes.BottomSheetFragment
import nbc.group.recipes.data.model.dto.BaseMapResponse
import nbc.group.recipes.BuildConfig.KAKAO_MAP_KEY
import nbc.group.recipes.ChipType
import nbc.group.recipes.R
import nbc.group.recipes.TestData
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
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showMapView()
        setSearch()
        observeViewModel()
        chipGroup()
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
                baseSettingMap()

                // Label 클릭리스너
                kakaomap.setOnLabelClickListener { kakaoMap, labelLayer, label ->
                    val bottomSheetFragment = BottomSheetFragment()
                    bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
                }

                // LodLabel 클릭리스너
                kakaoMap?.setOnLodLabelClickListener{ kakaoMap, labelLayer, label ->
                    val bottomSheetFragment = BottomSheetFragment()
                    bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
                }
            }

            override fun getZoomLevel(): Int {
                // 지도 시작 시 확대/축소 줌 레벨 설정 (8~10사이가 적당)
                return 8
            }
        })
    }


    private fun setSearch() = with(binding){

        searchEt.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){

                val searchText = searchEt.text.toString()

                // 뷰모델에 내가 입력한 텍스트값 전달
                if (searchText.isNotEmpty()) {
                    mapViewModel.getRegionSearch(searchText)
                } else {
                    Snackbar.make(searchEt, "검색어를 입력해주세요", Snackbar.LENGTH_SHORT).show()
                }

                // 키보드 내리기
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)

                // 텍스트값 제거
                searchEt.setText("")

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
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
        val styles = kakaoMap?.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_map_red)
            .setIconTransition(LabelTransition.from(Transition.Scale,Transition.Scale))))

        // styles가 null이 아닐때만, LabelOptions 생성하고 라벨추가
        if(styles != null){
            // LabelOptions 생성
            val options = LabelOptions.from(LatLng.from(latitude_formatter, longitude_formatter)).setStyles(styles)
            // LabelLayer 가져옴
            val layerOptions = LabelLayerOptions.from().setZOrder(2)     // setZOrder -> 우선순위 지정
            val layer = kakaoMap?.labelManager?.addLayer(layerOptions)  //  val layer = kakaoMap?.labelManager?.getLayer()
            // Label 생성
            layer?.addLabel(options)
        }else{
            Log.e("kakaoMap", "LabelStyles null값 에러")
        }

    }


    private fun observeViewModel(){
        viewLifecycleOwner.lifecycleScope.launch{

            mapViewModel.regionSearch.flowWithLifecycle(viewLifecycleOwner.lifecycle, STARTED).collectLatest{
                Log.d("it_data", it.toString()) // SearchResponse 전체가져옴

                // 검색결과에 documents가 포함되어있으면(무조건 포함함), 그 documents값중 첫번째값을 가져옴
                it?.documents?.firstOrNull()?.let { document ->
                    Log.d("documents__",document.toString()) // SearchDocumentsResponse의 첫번째값 가져옴
                    setMapData(document)
                }
            }
        }
    }


    // 시작하자마자 기본으로 보여지는 라벨(라벨 여러개 표시)
    private fun baseSettingMap(){

        val baseMapResponseLists = listOf(
            BaseMapResponse(id = 1, regionName = "천안", x_longitude = 127.113911, y_latitude = 36.815067),
            BaseMapResponse(id = 2, regionName = "이천", x_longitude = 127.435089, y_latitude = 37.272267),
            BaseMapResponse(id = 3, regionName = "동해", x_longitude = 129.114299, y_latitude = 37.524741),
            BaseMapResponse(id = 4, regionName = "서울", x_longitude = 126.978652, y_latitude = 37.566826),
            BaseMapResponse(id = 5, regionName = "파주", x_longitude = 126.779881, y_latitude = 37.760044),
        )

        val styles = kakaoMap?.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_map_blue)))

        if(styles != null){
            baseMapResponseLists.forEach{
                val options = LabelOptions.from(LatLng.from(it.y_latitude, it.x_longitude)).setStyles(styles)
                val layerOptions = LabelLayerOptions.from().setZOrder(1)
                val layer = kakaoMap?.labelManager?.addLodLayer(layerOptions)   // val layer = kakaoMap?.labelManager?.getLodLayer()
                layer?.addLodLabel(options)
            }
        }else{
            Log.e("kakaoMap", "LabelStyles null값 에러")
        }
    }


    private fun chipGroup(){
        binding.chipGroup.setOnCheckedStateChangeListener { chipGroup, checkedId ->

            Log.d("Chip",checkedId.toString())
            val selectChip = chipGroup.checkedChipId

            when(selectChip){
                R.id.fist_type -> chipType(ChipType.FIRST)
                R.id.second_type -> chipType(ChipType.SECOND)
                R.id.three_type -> chipType(ChipType.THIRD)
                R.id.four_type -> chipType(ChipType.FOURTH)
                R.id.five_type -> chipType(ChipType.FIFTH)
                R.id.six_type -> chipType(ChipType.SIXTH)
                else -> Log.d("chipGroup", "Unknown chip selected")
            }
        }
    }

    private fun chipType(type: ChipType){
        when(type){
            ChipType.FIRST -> {
                Log.d("type__",type.toString())
                mapViewModel.getRegionSearch(query = "인천")
            }
            ChipType.SECOND -> {
                mapViewModel.getRegionSearch(query = "대전")
            }
            ChipType.THIRD -> {
                mapViewModel.getRegionSearch(query = "논산")
            }
            ChipType.FOURTH -> {
                mapViewModel.getRegionSearch(query = "강릉")
            }
            ChipType.FIFTH -> {
                mapViewModel.getRegionSearch(query = "수원")
            }
            ChipType.SIXTH -> {
                mapViewModel.getRegionSearch(query = "세종")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        kakaoMap = null
    }

}
