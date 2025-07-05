package com.example.airpolution.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airpolution.data.Repository
import com.example.airpolution.data.remote.deepseek.PredictionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FutureStateUI(
    val text: String = "Press the button to predict",
    val moreInfo: String = "",
)

@HiltViewModel
class FutureViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _uiState = MutableStateFlow(FutureStateUI())
    val uiState = _uiState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(text = "Loading") }
            when (val prediction = repository.getPrediction()) {
                is PredictionResult.Success -> _uiState.update {
                    it.copy(text = prediction.text, moreInfo = prediction.moreInfo)
                }

                is PredictionResult.Error -> _uiState.update { it.copy(text = prediction.message) }
            }
        }
    }
}