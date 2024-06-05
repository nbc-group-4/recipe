package nbc.group.recipes.presentation.fragment

import android.Manifest
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.GlideApp
import nbc.group.recipes.R
import nbc.group.recipes.StorageGlideModule
import nbc.group.recipes.data.model.dto.Recipe
import nbc.group.recipes.data.network.FirebaseResult
import nbc.group.recipes.databinding.FragmentMypageBinding
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.viewmodel.MainViewModel
import java.io.File

@AndroidEntryPoint
class MypageFragment : Fragment() {

    companion object {
        const val TAG = "MypageFragment"
    }

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btSignIn.setOnClickListener(signInButtonClickListener)
            btLogOut.setOnClickListener(logOutButtonClickListener)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signInFlow.collect { nullable ->
                if(nullable == null) {
                    binding.clUser.visibility = View.GONE
                    binding.animationView.visibility = View.GONE
                    binding.clNonUser.visibility = View.VISIBLE
                }
                nullable?.let { nonNull ->
                    when(nonNull) {
                        is FirebaseResult.Success -> {
                            binding.clNonUser.visibility = View.GONE
                            binding.animationView.visibility = View.GONE
                            binding.clUser.visibility = View.VISIBLE
                        }
                        is FirebaseResult.Failure -> {
                            binding.clUser.visibility = View.GONE
                            binding.animationView.visibility = View.GONE
                            binding.clNonUser.visibility = View.VISIBLE
                        }
                        is FirebaseResult.Loading -> {
                            binding.clNonUser.visibility = View.GONE
                            binding.clUser.visibility = View.GONE
                            binding.animationView.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.e(TAG, "onDestroyView: ", )
    }

    private val signInButtonClickListener: (View) -> Unit = {
        (activity as MainActivity).moveToSignInFragment()
    }

    private val logOutButtonClickListener: (View) -> Unit = {
        viewModel.logout()
    }
}
