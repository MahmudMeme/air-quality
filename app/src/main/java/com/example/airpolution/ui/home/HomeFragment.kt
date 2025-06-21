package com.example.airpolution.ui.home

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.airpolution.databinding.FragmentHomeBinding
import com.example.airpolution.ui.home.adapter.AirMeasurementsAdapter
import com.example.airpolution.ui.home.dialogs.DialogUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.homeText.text = "Loading..."


        setupSpinner()

        val adapter = AirMeasurementsAdapter().apply {
            setOnItemClickListener { measurement ->
                DialogUtils.showMeasurementInfoDialog(requireContext(), measurement)
            }
        }


        binding.rcAirMeasurements.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rcAirMeasurements.adapter = adapter

        binding.rcAirMeasurements.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        binding.btnToday.setOnClickListener {
            homeViewModel.averageDataYesterday(0)
        }
        binding.btnyesterday.setOnClickListener {
            homeViewModel.averageDataYesterday(-1)
        }
        binding.btnTwoDaysBefore.setOnClickListener {
            homeViewModel.averageDataYesterday(-2)
        }
        binding.btnWeekly.setOnClickListener {
            homeViewModel.averageDataYesterday(1)
        }
        binding.btnMonthl.setOnClickListener {
            homeViewModel.averageDataYesterday(2)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiState.collect { state ->
                    binding.homeText.text = state.text
                    updateSpinner(state.cities)
                    adapter.updateData(state.airMeasurements)
                }
            }
        }

        return root
    }

    private fun updateSpinner(cities: List<String>) {
        val adapter = binding.cityList.adapter as ArrayAdapter<String>
        adapter.clear()
        adapter.addAll(cities)
        adapter.notifyDataSetChanged()

        binding.cityList.setSelection(0)
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            requireContext(), R.layout.simple_spinner_item, mutableListOf<String>()
        ).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }

        binding.cityList.adapter = adapter

        binding.cityList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                homeViewModel.handleCitySelected(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}