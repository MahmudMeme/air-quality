package com.example.airpolution.ui.home

//noinspection SuspiciousImport
import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.airpolution.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textHome.text = "Loading..."

        setupSpinner()

        homeViewModel.text.observe(viewLifecycleOwner) { text ->
            binding.textHome.text = text
        }


        return root
    }

    private fun setupSpinner() {
        val cities = listOf(
            "skopje",
            "ohrid",
            "bitola",
            "tetovo",
            "strumica",
            "gostivar"
        )
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            cities
        ).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }
        binding.cityList.adapter = adapter

        binding.cityList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCityName = cities[position]
                homeViewModel.fetchAirValues(selectedCityName)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
                //
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}