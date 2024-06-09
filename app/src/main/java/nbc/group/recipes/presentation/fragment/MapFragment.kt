package nbc.group.recipes.presentation.fragment

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
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
import nbc.group.recipes.data.model.dto.BaseMapResponse
import nbc.group.recipes.ChipType
import nbc.group.recipes.R
import nbc.group.recipes.data.model.dto.SearchDocumentsResponse
import nbc.group.recipes.data.network.KAKAO_MAP_KEY
import nbc.group.recipes.databinding.FragmentMapBinding
import nbc.group.recipes.viewmodel.MapViewModel
import java.lang.Exception
import java.util.Locale

@AndroidEntryPoint
class MapFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentMapBinding? = null

    private lateinit var mapView: MapView
    private var kakaoMap : KakaoMap? = null

    private val mapViewModel: MapViewModel by viewModels()
    private val sharedMapViewModel : MapViewModel by activityViewModels()

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
        deleteText()
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

                // Label 클릭리스너 (검색시)
                kakaomap.setOnLabelClickListener { kakaoMap, labelLayer, label ->

                    val searchText = binding.searchEt.text.toString()
                    // 특산품 데이터가 있는 지역이면
                    if (containsRegoin(searchText)){

                        val latitude = label.position.latitude
                        val longitude = label.position.longitude

                        val regionName = getRegionName(latitude, longitude)
                        Log.d("regionName___", regionName)  // 통영

                        binding.searchEt.setText(regionName)

                        if (regionName.isNotEmpty()){
                            // 라벨이 클릭될때, 지역명을 관찰해서 특산물데이터 받아옴
                            sharedMapViewModel.getSpecialtie(regionName)

                            val bottomSheetFragment = BottomSheetFragment()
                            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
                        }else{
                            Snackbar.make(binding.searchEt, "지역명을 입력해주세요", Snackbar.LENGTH_SHORT).show()
                        }


                    }else{
                        // 특산품 데이터가 없는 지역일경우
                        val latitude = label.position.latitude
                        val longitude = label.position.longitude
                        val regionName = AllgetRegionName(latitude, longitude)
                        Log.d("regionNamessdsd___", regionName)
                        binding.searchEt.setText(regionName)

                        Snackbar.make(binding.searchEt, "해당지역은 특산물 데이터가 없습니다", Snackbar.LENGTH_SHORT).show()
                    }
                }

                // LodLabel 클릭리스너 (기본세팅)
                kakaoMap?.setOnLodLabelClickListener{ kakaoMap, labelLayer, label ->

                    // 마커 클릭시, 클릭한 지역의 위도,경도 받아오기
                    // 그 위도,경도에 맞는 지역이름을 가져옴
                    // 그 지역이름을 search EditText에 표시

                    val latitude = label.position.latitude
                    val longitude = label.position.longitude

                    val regionName = getRegionName(latitude, longitude)
                    Log.d("regionName_si__", regionName)  // 통영

                    binding.searchEt.setText(regionName)

                    // 라벨이 클릭될때, 지역명을 관찰
                    sharedMapViewModel.getSpecialtie(regionName)

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

                // 검색할 수 있는 지역이 포함되어있으면
                if (AllcontainsRegoin(searchText)){
                    mapViewModel.getRegionSearch(searchText)
                }else{
                    // 지역이 아닌 다른것이 포함되어있으면 (영어,기호,외계어..)
                    Snackbar.make(searchEt, "검색할수없는 지역입니다", Snackbar.LENGTH_SHORT).show()
                }

                // 키보드 내리기
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireActivity().window.decorView.applicationWindowToken, 0)

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
            BaseMapResponse(id = 1, regionName = "군산", x_longitude = 126.736840, y_latitude = 35.967466),
            BaseMapResponse(id = 2, regionName = "김포", x_longitude = 126.715657, y_latitude = 37.615268),
            BaseMapResponse(id = 3, regionName = "양주", x_longitude = 127.045786, y_latitude = 37.785313),
            BaseMapResponse(id = 4, regionName = "논산", x_longitude = 127.098734, y_latitude = 36.187183),
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
                mapViewModel.getRegionSearch(query = "통영")
                binding.searchEt.setText("통영")
            }
            ChipType.SECOND -> {
                mapViewModel.getRegionSearch(query = "대전")
                binding.searchEt.setText("대전")
            }
            ChipType.THIRD -> {
                mapViewModel.getRegionSearch(query = "안성")
                binding.searchEt.setText("안성")
            }
            ChipType.FOURTH -> {
                mapViewModel.getRegionSearch(query = "파주")
                binding.searchEt.setText("파주")
            }
            ChipType.FIFTH -> {
                mapViewModel.getRegionSearch(query = "서산")
                binding.searchEt.setText("서산")
            }
            ChipType.SIXTH -> {
                mapViewModel.getRegionSearch(query = "세종")
                binding.searchEt.setText("세종")
            }
        }
    }


    // 특산품에 대한 데이터가 있는 지역리스트
    private val regions = listOf("통영","대전","군산","김포","양주","논산","통영","안성","파주","세종","인천",
        "원주","서산","함양","성주","영암","부산","봉화","정읍","횡성","삼척","평택","창녕"
        ,"거제","진주","울릉","포항","영암","옹진","충주","상주","김천","대구","영천","경주",
        "울산","김해","통영","여수","나주","전주","보령","제천","영주","완도")


    // 모든 지역리스트 (추후에 더 추가예정..)
    private val Allregions = listOf("통영","대전","군산","김포","양주","논산","통영","대전","안성","파주","세종","인천",
        "원주","서산","함양","성주","영암","부산","봉화","정읍","횡성","삼척","평택","창녕"
        ,"거제","진주","울릉","포항","영암","옹진","충주","상주","김천","대구","영천","경주",
        "울산","김해","통영","여수","나주","전주","보령","제천","영주","완도","영종도","포천","수원","서울","과천"
        ,"광주","포천","화성","오산","이천","춘천","속초","강릉","당진")



    // 위도,경도를 통해 지역명 가져오는 함수
    private fun getRegionName(latitude: Double, longitude: Double) : String {
        // Geocoder는 위,경도 좌표 이용해서 해당위치의 주소정보 가져올수있는 클래스
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        // 위도, 경도 정보로부터 주소정보 가져오기
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        // 주소 정보에서 "시" 단위 지역명 추출
        val locality = addresses?.firstOrNull()?.locality ?: "${addresses}"
        Log.d("asda", locality)

        // 지역명이 regions 리스트에 포함되어 있는지 확인하고 반환
        return regions.firstOrNull { locality.contains(it) } ?: ""
    }


    // 위도,경도를 통해 지역명 가져오는 함수(전체)
    private fun AllgetRegionName(latitude: Double, longitude: Double) : String {
        // Geocoder는 위,경도 좌표 이용해서 해당위치의 주소정보 가져올수있는 클래스
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        // 위도, 경도 정보로부터 주소정보 가져오기
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        // 주소 정보에서 "시" 단위 지역명 추출
        val locality = addresses?.firstOrNull()?.locality ?: "${addresses}"

        // 지역명이 Allregions 리스트에 포함되어 있는지 확인하고 반환
        return Allregions.firstOrNull { locality.contains(it) } ?: ""
    }

    private fun deleteText() = with(binding){
        deleteIv.setOnClickListener {
            // 텍스트값 제거
            searchEt.setText("")
            Snackbar.make(mapView, "검색어가 삭제되었습니다", Snackbar.LENGTH_SHORT).show()
        }
    }


    // 특산품에 대한 데이터가 있는 지역리스트
    fun containsRegoin(searchText: String): Boolean {
        return regions.any { searchText.contains(it) }
    }


    // 검색이 되는 모든 지역리스트
    fun AllcontainsRegoin(searchText: String): Boolean {
        return Allregions.any { searchText.contains(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        kakaoMap = null
    }

}


