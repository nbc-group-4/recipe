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
            R.drawable.img_banner1 -> getString(R.string.banner_text_1)
            R.drawable.img_banner2 -> getString(R.string.banner_text_2)
            R.drawable.img_banner3 -> getString(R.string.banner_text_3)
            R.drawable.img_banner4 -> getString(R.string.banner_text_4)
            R.drawable.img_banner5 -> getString(R.string.banner_text_5)
            R.drawable.img_banner6 -> getString(R.string.banner_text_6)
            else -> getString(R.string.banner_text_7)
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
