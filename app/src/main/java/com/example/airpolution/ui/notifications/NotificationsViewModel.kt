package com.example.airpolution.ui.notifications

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.ViewModel
import com.example.airpolution.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SettingsStateUI(
    val text: String = " Selected default city "
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsStateUI())
    val uiState = _uiState.asStateFlow()


    fun setDefaultCity(context: Context, city: String) {
        val sp = context.getSharedPreferences("airCity", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("defaultCity", city)
        editor.apply()
    }

    fun getDefaultCityFromSp(context: Context): String? {
        val sp = context.getSharedPreferences("airCity", MODE_PRIVATE)
        val city = sp.getString("defaultCity", null)
        return city
    }
}