package com.example.airpolution.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.airpolution.R
import com.example.airpolution.databinding.FragmentFutureBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FutureFragment : Fragment() {

    private var _binding: FragmentFutureBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("ResourceAsColor")
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
        binding.btnMoreInfo.setOnClickListener {
            showInfoDialog(futureViewModel.uiState.value.moreInfo)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                futureViewModel.uiState.collect { state ->
                    binding.textDashboard.text = state.text

                    val boolean = state.moreInfo.isNotEmpty()
                    binding.btnMoreInfo.isEnabled = state.moreInfo.isNotEmpty()
                    if (boolean) {
                        binding.btnMoreInfo.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.button_text_color
                            )
                        )
                    }

                    binding.btnPredict.isEnabled = isLoadedData(state.text)
                }
            }
        }
        return root
    }

    private fun isLoadedData(data: String): Boolean {
        return !data.startsWith("Date")
    }

    private fun showInfoDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Additional Information")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}