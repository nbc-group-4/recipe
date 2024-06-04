package nbc.group.recipes.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.ktx.Firebase
import nbc.group.recipes.R
import nbc.group.recipes.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.data.network.FirebaseResult
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.viewmodel.MainViewModel

@AndroidEntryPoint
class SignInFragment : Fragment() {

    companion object {
        const val TAG = "SignInFragment"
    }

    private var _binding: FragmentSignInBinding? = null
    val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btSignIn.setOnClickListener(signInButtonClickListener)
            tvSignUpButton.setOnClickListener(signUpButtonClickListener)
            tvSearchPwButton.setOnClickListener(searchPwButtonClickListener)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signInFlow.collect { nullable ->
                nullable?.let { nonNull ->
                    when(nonNull) {
                        is FirebaseResult.Success -> {
                            findNavController().popBackStack()
                        }
                        is FirebaseResult.Failure -> {
                            Log.e(TAG, "onViewCreated: Failure", )
                        }
                        is FirebaseResult.Loading -> {

                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val signInButtonClickListener: (View) -> Unit = {
        with(binding) {
            viewModel.signIn(
                etId.text.toString(),
                etPw.text.toString()
            )
        }
    }

    private val signUpButtonClickListener: (View) -> Unit = {
        (activity as MainActivity).moveToSignUpFragment()
    }

    private val searchPwButtonClickListener: (View) -> Unit = {

    }
}
