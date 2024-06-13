package nbc.group.recipes.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.data.network.FirebaseResult
import nbc.group.recipes.databinding.FragmentSignUpBinding
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.viewmodel.MainViewModel

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btSignUp.setOnClickListener(signUpButtonClickListener)
            ivBackButton.setOnClickListener(backButtonClickListener)
            etPwCheck.setOnFocusChangeListener(pwCheckFocusChangeListener)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signUpFlow.collect { nullable ->
                nullable?.let { nonNull ->
                    when (nonNull) {
                        is FirebaseResult.Success -> {
                            findNavController().popBackStack()
                        }

                        is FirebaseResult.Failure -> {
                            binding.clLoading.visibility = View.GONE
                            binding.tvErrorIndic.text = nonNull.exception.message
                        }

                        is FirebaseResult.Loading -> {
                            binding.clLoading.visibility = View.VISIBLE

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

    private val signUpButtonClickListener: (View) -> Unit = {
        with(binding) {
            viewModel.signUp(
                name = etNickname.text?.toString()?: "",
                id = etId.text?.toString()?: "",
                pw = etPw.text?.toString()?: ""
            )
        }
    }

    private val backButtonClickListener: (View) -> Unit = {
        (activity as MainActivity).moveToBack()
    }

    private val pwCheckFocusChangeListener: (View, Boolean) -> Unit = { view, hasFocus ->
        if(!hasFocus) {
            with(binding) {
                if(etPw.text.toString() != etPwCheck.text.toString()) {
                    etPwCheckLayout.error = "비밀번호를 정확하게 입력하세요."
                } else {
                    etPwCheckLayout.error = null
                }
            }
        }
    }

}