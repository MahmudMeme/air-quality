package com.example.airpolution.ui.notifications

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.airpolution.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val notificationsViewModel: NotificationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupSpinner()

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    private fun setupSpinner() {

        val allCities = resources.getStringArray(com.example.airpolution.R.array.cities_list).toList()

        val savedCity = notificationsViewModel.getDefaultCityFromSp(requireContext())

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
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {

                    val selectedCityName = orderedCities[position]
                    notificationsViewModel.setDefaultCity(requireContext(), selectedCityName)
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