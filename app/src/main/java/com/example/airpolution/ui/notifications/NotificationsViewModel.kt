package com.example.airpolution.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airpolution.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsStateUI(
    val text: String = " Selected default city ",
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsStateUI())
    val uiState = _uiState.asStateFlow()
    var cityDef: String? = ""

    fun setDefaultCity(city: String) {
        viewModelScope.launch {
            repository.setDefaultCity(city)
        }
    }

    fun getDefaultCityFromSp(): String? {
        viewModelScope.launch {
            cityDef = repository.getDefaultCityFromSp()
        }
        return cityDef
    }
}