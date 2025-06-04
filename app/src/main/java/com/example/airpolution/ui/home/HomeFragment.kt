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
import com.example.airpolution.databinding.FragmentHomeBinding
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

        binding.textHome.text = "Loading..."


        val cityTemp = homeViewModel.getTempCityFromSp()
        val citySP = homeViewModel.getDefaultCityFromSp()
        if (cityTemp == null) {
            setupSpinner(citySP)
        } else {
            setupSpinner(cityTemp)
        }



        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiState.collect { state ->
                    binding.textHome.text = state.text
                }
            }
        }

        return root
    }

    private fun setupSpinner(citySp: String?) {

        val allCities =
            resources.getStringArray(com.example.airpolution.R.array.cities_list).toList()
        val orderedCities = if (citySp != null && allCities.contains(citySp)) {
            listOf(citySp) + allCities.filter { it != citySp }
        } else {
            allCities
        }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            orderedCities
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
                val selectedCityName = orderedCities[position]
                homeViewModel.fetchAirValues(selectedCityName)

                homeViewModel.setTemporaryCity(selectedCityName)


                val newOrderedCities =
                    listOf(selectedCityName) + allCities.filter { it != selectedCityName }

                adapter.clear()
                adapter.addAll(newOrderedCities)
                adapter.notifyDataSetChanged()

                binding.cityList.setSelection(0)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        //ne vleguva vo ovoj metod bug??
        homeViewModel.removeTempCityFromSp()
        super.onDestroy()
    }
}