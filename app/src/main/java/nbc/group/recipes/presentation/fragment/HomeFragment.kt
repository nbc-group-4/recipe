package nbc.group.recipes.presentation.fragment

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import nbc.group.recipes.databinding.FragmentHomeBinding
import nbc.group.recipes.KindItem
import nbc.group.recipes.R
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.presentation.adapter.BannerAdapter
import nbc.group.recipes.presentation.adapter.HomeKindAdapter
import nbc.group.recipes.presentation.adapter.HomeQuizAdapter
import nbc.group.recipes.specialtyKind
import nbc.group.recipes.specialtyKindMore
import nbc.group.recipes.viewmodel.MainViewModel
import nbc.group.recipes.viewmodel.SpecialtyViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private var homeQuizAdapter: HomeQuizAdapter? = null
    private var homeKindAdapter: HomeKindAdapter? = null
    private val specialtyViewModel: SpecialtyViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeQuizAdapter = HomeQuizAdapter()
        homeKindAdapter = HomeKindAdapter(object : HomeKindAdapter.OnItemClickListener {
            override fun onClick(data: KindItem) {
                navigateToSpecialty(data)
                sendSpecialtyKind(data)
            }
        })

        setupRecyclerViewKind()

//        // 더보기 클릭
//        binding.btnHomeKindMore.setOnClickListener {
//            loadMoreItems()
//        }

        // Splash 종료
        mainViewModel.homeFragmentStatusChange()
        if(!isInternetConnection()) {
            showDialog()
        }
        banner()
    }

    private fun setupRecyclerViewKind() {
        binding.recyclerViewHomeKind.apply {
            adapter = homeKindAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)

        }
        homeKindAdapter?.submitList(specialtyKind)
    }

    private fun loadMoreItems() {
        val currentList = homeKindAdapter?.currentList?.toMutableList()
        if (currentList != null) {
            if (currentList.size == specialtyKind.size) {
                currentList.addAll(specialtyKindMore.take(2))
            }
        }
        homeKindAdapter?.submitList(currentList)
    }

    private fun navigateToSpecialty(item: KindItem) {
        specialtyViewModel.setSelectedKindItem(item)
        (activity as MainActivity).moveToSpecialtyFragment()
    }

    private fun sendSpecialtyKind(item: KindItem) {
        specialtyViewModel.setSelectedKindItem(item)
    }

    private fun isInternetConnection(): Boolean {
        val cm = requireActivity()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        homeQuizAdapter = null
        homeKindAdapter = null
    }


    private fun banner(){

        val bannerAdapter = BannerAdapter(this)
        bannerAdapter.addImg(BannerFragment(R.drawable.img_banner1, "https://terms.naver.com/search.naver?query=%EC%98%A5%EC%88%98%EC%88%98&searchType=&dicType=&subject="))
        bannerAdapter.addImg(BannerFragment(R.drawable.img_banner2, "https://terms.naver.com/search.naver?query=%EA%B0%90%EC%9E%90&searchType=text&dicType=&subject="))
        bannerAdapter.addImg(BannerFragment(R.drawable.img_banner3, "https://terms.naver.com/search.naver?query=%EA%B0%80%EB%A6%AC%EB%B9%84&searchType=text&dicType=&subject="))
        bannerAdapter.addImg(BannerFragment(R.drawable.img_banner4, "https://terms.naver.com/search.naver?query=%ED%99%8D%EA%B2%8C&searchType=text&dicType=&subject="))
        bannerAdapter.addImg(BannerFragment(R.drawable.img_banner5, "https://terms.naver.com/search.naver?query=%EC%83%88%EA%BC%AC%EB%A7%89&searchType=text&dicType=&subject="))
        bannerAdapter.addImg(BannerFragment(R.drawable.img_banner6, "https://terms.naver.com/search.naver?query=%EC%88%98%EB%B0%95&searchType=text&dicType=&subject="))
        bannerAdapter.addImg(BannerFragment(R.drawable.img_banner7, "https://terms.naver.com/search.naver?query=%EB%B0%A4%ED%98%B8%EB%B0%95&searchType=text&dicType=&subject="))


        with(binding){
            homeBanner.adapter = bannerAdapter
            homeBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            homeBannerIndicator.setViewPager(binding.homeBanner)
        }
    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(requireActivity())
            .setTitle("인터넷이 필요한 서비스입니다.")
            .setPositiveButton("확인") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setOnDismissListener {
                requireActivity().finish()
            }
        dialog.show()
    }

}