package nbc.group.recipes.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import nbc.group.recipes.R
import nbc.group.recipes.databinding.FragmentMainBinding
import nbc.group.recipes.viewmodel.MainViewModel

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNavigation()

        binding.testButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_signInFragment)
        }

        binding.testButtonLogout.setOnClickListener {
            viewModel.logout()
            Log.e("TAG", "onViewCreated: ${viewModel.currentUser}", )
        }
    }

    private fun initNavigation() {
        val navHostFragment = binding.fcv.getFragment<NavHostFragment>()
        val navController = navHostFragment.navController
        binding.bottomNavBar.setupWithNavController(navController)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}