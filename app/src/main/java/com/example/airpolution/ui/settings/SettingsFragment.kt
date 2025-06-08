package com.example.airpolution.ui.settings

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.airpolution.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val notificationsViewModel: NotificationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupSpinner()

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                notificationsViewModel.uiState.collect { state ->
                    binding.textNotifications.text = state.text
                }
            }
        }

        return root
    }

    private fun setupSpinner() {

        val allCities =
            resources.getStringArray(com.example.airpolution.R.array.cities_list).toList()

        val savedCity = notificationsViewModel.getDefaultCityFromSp()
            val orderedCities = if (savedCity != null && allCities.contains(savedCity)) {
                listOf(savedCity) + allCities.filter { it != savedCity }
            } else {
                allCities
            }


            val adapter = ArrayAdapter(
                requireContext(), R.layout.simple_spinner_item, orderedCities
            ).apply {
                setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            }
            binding.cityListSetings.adapter = adapter

            binding.cityListSetings.setSelection(0)

            binding.cityListSetings.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long,
                    ) {

                        val selectedCityName = orderedCities[position]
                        viewLifecycleOwner.lifecycleScope.launch {
                            notificationsViewModel.setDefaultCity(selectedCityName)
                        }

                        val newOrderedCities =
                            listOf(selectedCityName) + allCities.filter { it != selectedCityName }

                        adapter.clear()
                        adapter.addAll(newOrderedCities)
                        adapter.notifyDataSetChanged()

                        binding.cityListSetings.setSelection(0)

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}