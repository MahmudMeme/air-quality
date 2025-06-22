package com.example.airpolution.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.airpolution.databinding.FragmentFutureBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FutureFragment : Fragment() {

    private var _binding: FragmentFutureBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val futureViewModel = ViewModelProvider(this).get(FutureViewModel::class.java)

        _binding = FragmentFutureBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.btnPredict.setOnClickListener {
            futureViewModel.loadData()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                futureViewModel.uiState.collect { state ->
                    binding.textDashboard.text = state.text
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}