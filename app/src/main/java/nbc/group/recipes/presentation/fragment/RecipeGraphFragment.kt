package nbc.group.recipes.presentation.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nbc.group.recipes.data.network.NetworkResult
import nbc.group.recipes.databinding.FragmentRecipeGraphBinding
import nbc.group.recipes.presentation.MainActivity
import nbc.group.recipes.viewmodel.MainViewModel
import nbc.group.recipes.viewmodel.RecipeGraphViewModel

@AndroidEntryPoint
class RecipeGraphFragment : Fragment() {

    private var _binding: FragmentRecipeGraphBinding? = null
    private val binding
        get() = _binding!!

    private val recipeGraphViewModel: RecipeGraphViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    
    private var targetIndex: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecipeGraphBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGraph()
        observeViewModel()

        binding.btMakeRecipe.setOnClickListener(makeButtonClickListener)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    recipeGraphViewModel.lottieSwitch.collectLatest {
                        binding.lav.visibility = View.GONE
                    }
                }

                launch {
                    recipeGraphViewModel.currentSpecialty.collectLatest {
                        it?.let { currentSpecialty ->
                            if(recipeGraphViewModel.history != currentSpecialty) {
                                binding.gv.setValue(emptyList(), emptyList())
                            }
                            if(binding.gv.getNodes().size == 0) {
                                binding.gv.addNode(currentSpecialty, null)
                                recipeGraphViewModel.history = recipeGraphViewModel.currentSpecialty.value
                            }
                        }
                    }
                }

                launch {
                    recipeGraphViewModel.recipes.collect { nullable ->
                        nullable?.let { nonNull ->
                            when(nonNull) {
                                is NetworkResult.Success -> {
                                    nonNull.result.forEach {
                                        binding.gv.addNode(it, targetIndex)
                                    }
                                    binding.lav.visibility = View.GONE
                                    recipeGraphViewModel.initRecipeFlow()
                                }
                                is NetworkResult.Failure -> {

                                }
                                is NetworkResult.Loading -> {

                                }
                            }
                        }
                    }
                }

                launch {
                    recipeGraphViewModel.nodes.collect { nodes ->
                        if(nodes.isNotEmpty()) {
                            binding.gv.setNodes(nodes)
                        }
                    }
                }

                launch {
                    recipeGraphViewModel.edges.collect { edges ->
                        if(edges.isNotEmpty()) {
                            binding.gv.setEdges(edges)
                        }
                    }
                }
            }
        }
    }

    private fun initGraph() {
        with(binding.gv) {
            registerLongClickListener { specialty, index ->
                binding.lav.visibility = View.VISIBLE
                targetIndex = index
                recipeGraphViewModel.getRecipes(specialty)
            }
            registerSingleTapUpListener { recipeEntity ->
                val bundle = Bundle().apply { putParcelable("recipeDetail", recipeEntity) }
                (activity as MainActivity).moveToRecipeDetailFragment(bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        recipeGraphViewModel.storeGraphElem(
            binding.gv.getNodes(),
            binding.gv.getEdges()
        )


        _binding = null
    }

    private val makeButtonClickListener: (View) -> Unit = {
        if(mainViewModel.currentUser == null) {
            showDialog()
        } else {
            (activity as MainActivity).moveToMakeRecipeFragment()
        }
    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(requireActivity())
            .setTitle("로그인이 필요한 서비스입니다.")
            .setPositiveButton("확인") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        dialog.show()
    }
}