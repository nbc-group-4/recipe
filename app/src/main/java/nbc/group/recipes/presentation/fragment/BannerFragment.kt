package nbc.group.recipes.presentation.fragment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nbc.group.recipes.R
import nbc.group.recipes.databinding.DialogLinkConfirmBinding
import nbc.group.recipes.databinding.FragmentBannerBinding

class BannerFragment(private val imgRes : Int, private val url: String) : Fragment(){

    private val binding get() = _binding!!
    private var _binding: FragmentBannerBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        banner()
    }


    private fun banner(){
        binding.homeBakgroundIv.setImageResource(imgRes)

        val bannerText = when(imgRes){
            R.drawable.img_banner1 -> "6월의 달콤한 선물,\n신선한 제철 옥수수!"
            R.drawable.img_banner2 -> "제철감자로 즐기는,\n여름의 맛있는 순간!"
            R.drawable.img_banner3 -> "6월의 바다향기,\n신선한 제철 홍가리비!"
            R.drawable.img_banner4 -> "여름의 진미,\n제철 홍게로 풍성하게!"
            R.drawable.img_banner5 -> "6월의 풍성함,\n제철 새꼬막으로!"
            R.drawable.img_banner6 -> "제철수박으로 즐기는,\n시원한 여름휴가!"
            else -> "여름의 달콤한 감동,\n제철 밤호박을 만나보세요!"
        }
        binding.homeBannerTv.text = bannerText

        binding.homeBakgroundIv.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialogBinding = DialogLinkConfirmBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.dialogMoveBtn.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            alertDialog.dismiss()
        }

        dialogBinding.dialogStayBtn.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
