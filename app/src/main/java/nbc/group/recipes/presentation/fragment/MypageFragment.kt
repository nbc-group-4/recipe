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
import androidx.recyclerview.widget.GridLayoutManager
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
import nbc.group.recipes.presentation.adapter.MyPageRecipeAdapter
import nbc.group.recipes.presentation.adapter.decoration.GridSpacingItemDecoration
import nbc.group.recipes.viewmodel.MainViewModel
import java.io.File

@AndroidEntryPoint
class MypageFragment : Fragment() {

    companion object {
        const val TAG = "MypageFragment"
    }

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private var _adapter: MyPageRecipeAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: MainViewModel by activityViewModels()

    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if(uri != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                val inputStream = requireActivity().contentResolver.openInputStream(uri)
                viewModel.putImage(inputStream!!)
                binding.ivUserProfile.setImageURI(uri)
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

        _adapter = MyPageRecipeAdapter(this)

        with(binding) {
            btSignIn.setOnClickListener(signInButtonClickListener)
            tvResignButton.setOnClickListener(resignButtonClickListener)
            tvLogOutButton.setOnClickListener(logOutButtonClickListener)
            ivUserProfile.setOnClickListener(userProfileImageClickListener)
            rvUserRecipe.layoutManager = GridLayoutManager(activity, 2)
            rvUserRecipe.adapter = adapter
            rvUserRecipe.addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    (20 * resources.displayMetrics.density + 0.5f).toInt(),
                    false
                )
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signInFlow.collect { nullable ->
                if(nullable == null) {
                    binding.clUser.visibility = View.GONE
                    binding.clNonUser.visibility = View.VISIBLE
                }
                nullable?.let { nonNull ->
                    when(nonNull) {
                        is FirebaseResult.Success -> {
                            binding.clNonUser.visibility = View.GONE
                            binding.clUser.visibility = View.VISIBLE
                            initUser()
                        }
                        is FirebaseResult.Failure -> {
                            binding.clUser.visibility = View.GONE
                            binding.clNonUser.visibility = View.VISIBLE
                        }
                        is FirebaseResult.Loading -> {

                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userMetaData.collect { nullable ->
                nullable?.let { nonNull ->
                    when(nonNull) {
                        is FirebaseResult.Success -> {
                            Log.e(TAG, "onViewCreated: get recipeIds: Success", )
                            adapter.submitList(nonNull.result.recipeIds)
                        }
                        is FirebaseResult.Failure -> {
                            Log.e(TAG, "onViewCreated: get recipeIds: Failure: ${nonNull.exception}", )
                        }
                        is FirebaseResult.Loading -> {
                            Log.e(TAG, "onViewCreated: get recipeIds: Loading", )
                        }
                    }
                }
            }
        }
    }

    private fun initUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser?.let { currentUser ->
                binding.tvUserName.text = currentUser.displayName
                GlideApp.with(this@MypageFragment)
                    .load(Firebase.storage.reference
                        .child("userProfile/${currentUser.uid}/profile.jpg"))
                    .error(R.drawable.ic_appbar_mypage)
                    .into(binding.ivUserProfile)
                viewModel.getUserMeta(currentUser.uid)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    private val signInButtonClickListener: (View) -> Unit = {
        (activity as MainActivity).moveToSignInFragment()
    }

    private val resignButtonClickListener: (View) -> Unit = {
        viewModel.resign()
        viewModel.logout()
    }

    private val logOutButtonClickListener: (View) -> Unit = {
        viewModel.logout()
    }

    private val userProfileImageClickListener: (View) -> Unit = {
        pickMedia.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
}
