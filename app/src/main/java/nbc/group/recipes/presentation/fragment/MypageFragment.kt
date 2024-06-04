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

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if(isGranted) {
                Log.e(TAG, "Permission Granted")
            }
        }

    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if(uri != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                val inputStream = requireActivity().contentResolver.openInputStream(uri)
                viewModel.putImage(inputStream!!)
                binding.testImageView.setImageURI(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissionLauncher.launch(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        binding.testImageView2.setOnClickListener {
            GlideApp.with(this)
                .load(Firebase.storage.reference.child("userProfile/jun/profile.jpg"))
                .into(binding.testImageView2)
        }

        with(binding) {
            testButton1.setOnClickListener {
                viewModel.putRecipe(
                    Recipe()
                )
            }
            testButton2.setOnClickListener {
                viewModel.getRecipe()
            }
            testImageView.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
            fab.setOnClickListener {
                (activity as MainActivity).moveToSignInFragment()
            }
        }

        viewModel.currentUser?.let {
            binding.tv.text = "${it.email}"
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signInFlow.collect { nullable ->
                if(nullable == null) binding.tv.text = "There is no user"
                nullable?.let { nonNull ->
                    when(nonNull) {
                        is FirebaseResult.Success -> {
                            binding.tv.text = nonNull.result.email
                        }
                        is FirebaseResult.Failure -> {
                            Log.e(SignInFragment.TAG, "onViewCreated: Failure", )
                            binding.tv.text = ""
                        }
                        is FirebaseResult.Loading -> {

                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recipes.collect { nullable ->
                if(nullable == null) binding.tv.text = "There are no recipes"
                nullable?.let { nonNull ->
                    when(nonNull) {
                        is FirebaseResult.Success -> {
                            binding.tv.text = nonNull.result.toString()
                        }
                        is FirebaseResult.Failure -> {
                            Log.e(SignInFragment.TAG, "onViewCreated: Failure: ${nonNull.exception}", )
                            binding.tv.text = ""
                        }
                        is FirebaseResult.Loading -> {

                        }
                    }
                }
            }
        }

//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.user.collect {
//                it?.let { user ->
//                    binding.tv.text = "${user.email}"
//                }
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.e(TAG, "onDestroyView: ", )
    }

}