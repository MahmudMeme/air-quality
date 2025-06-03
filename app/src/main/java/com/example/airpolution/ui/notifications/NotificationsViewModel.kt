package com.example.airpolution.ui.notifications

import androidx.lifecycle.ViewModel
import com.example.airpolution.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SettingsStateUI(
    val text: String = " Selected default city ",
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsStateUI())
    val uiState = _uiState.asStateFlow()

    suspend fun setDefaultCity(city: String) {
        repository.setDefaultCity(city)
    }

    suspend fun getDefaultCityFromSp(): String? {
        return repository.getDefaultCityFromSp()
    }
}