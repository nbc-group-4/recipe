package nbc.group.recipes.presentation.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
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

@AndroidEntryPoint // -> 프래그먼트에 Hilt 설정, 생명 주기 동안 의존성 주입 및 관리 가능
// Hilt를 통해 ViewModel 주입 : by activityViewModels()
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

            etId.addTextChangedListener(textWatcher)
            etPw.addTextChangedListener(textWatcher)
            etPwCheck.addTextChangedListener(textWatcher)
            etNickname.addTextChangedListener(textWatcher)
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


    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateButton()
        }
        override fun afterTextChanged(s: Editable?) {
        }
    }

    private fun updateButton() {
        with(binding) {
            val emailFlag = etId.text.toString().isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(etId.text.toString()).matches()
            val passFlag = etPw.text.toString().length >= 6
            val passCheckFlag = etPw.text.toString() == etPwCheck.text.toString()
            val nicknameFlag = etNickname.text.toString().isNotEmpty()

            val allFieldsValid = emailFlag && passFlag && passCheckFlag && nicknameFlag

            if (allFieldsValid) {
                btSignUp.isEnabled = true
                btSignUp.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green1))
            } else {
                btSignUp.isEnabled = false
                btSignUp.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.unclick_btn))
            }
        }
    }

}