package org.guivicj.support.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.guivicj.support.domain.model.TechnicianDTO
import org.guivicj.support.domain.repository.TechRepository

class TechViewModel(
    private val techRepository: TechRepository
) : ViewModel() {

    private val _allTechnicians = MutableStateFlow<List<TechnicianDTO>>(emptyList())
    val allTechnicians = _allTechnicians.asStateFlow()
    private var errorMessage by mutableStateOf<String?>(null)

    fun fetchTechnicians() {
        viewModelScope.launch {
            try {
                val technicians = techRepository. getTechnicians()
                _allTechnicians.value = technicians
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
