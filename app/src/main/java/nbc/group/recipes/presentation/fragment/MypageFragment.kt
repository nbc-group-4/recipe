package nbc.group.recipes.presentation.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.R
import nbc.group.recipes.data.network.FirebaseResult
import nbc.group.recipes.databinding.FragmentMypageBinding
import nbc.group.recipes.viewmodel.MainViewModel

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