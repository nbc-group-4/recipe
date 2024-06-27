package nbc.group.recipes.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nbc.group.recipes.R
import nbc.group.recipes.data.network.NetworkResult
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
            checkboxAll.setOnClickListener { onCheckedTerms(checkboxAll) }
            checkbox1.setOnClickListener { onCheckedTerms(checkbox1) }
            checkbox2.setOnClickListener { onCheckedTerms(checkbox2) }

            ivCheckbox2Arrow.setOnClickListener {
                (activity as MainActivity).moveToPrivacyPolicyFragment()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signUpFlow.collect { nullable ->
                nullable?.let { nonNull -> with(binding){
                        when (nonNull) {
                            is NetworkResult.Success -> {
                                if (checkboxAll.isChecked){
                                    findNavController().popBackStack()
                                }else{
                                    tvErrorIndic.text = "모든 약관에 동의해주세요"
                                }
                            }

                            is NetworkResult.Failure -> {
                                clLoading.visibility = View.GONE
                                tvErrorIndic.text = nonNull.exception.message
                            }

                            is NetworkResult.Loading -> {
                                clLoading.visibility = View.VISIBLE
                            }
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
            if (checkboxAll.isChecked){
                viewModel.signUp(
                    name = etNickname.text?.toString()?: "",
                    id = etId.text?.toString()?: "",
                    pw = etPw.text?.toString()?: ""
                )
            }else{
                tvErrorIndic.text = "모든 약관에 동의해주세요"
            }
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

    private fun onCheckedTerms(compoundButton: CompoundButton){
        with(binding){
            when(compoundButton.id){
                R.id.checkbox_all -> {
                    if(checkboxAll.isChecked){
                        checkbox1.isChecked = true
                        checkbox2.isChecked = true
                    }else{
                        checkbox1.isChecked = false
                        checkbox2.isChecked = false
                    }
                }else -> {
                checkboxAll.isChecked = (checkbox1.isChecked && checkbox2.isChecked)
                }
            }
        }
    }

}