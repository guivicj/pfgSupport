package org.guivicj.support.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.guivicj.support.domain.model.AppStatsDTO
import org.guivicj.support.domain.model.TechnicianDTO
import org.guivicj.support.domain.repository.TechRepository
import org.guivicj.support.firebase.FirebaseTokenProvider

class TechViewModel(
    private val techRepository: TechRepository,
    private val tokenProvider: FirebaseTokenProvider
) : ViewModel() {

    private val _allTechnicians = MutableStateFlow<List<TechnicianDTO>>(emptyList())
    val allTechnicians = _allTechnicians.asStateFlow()
    private var errorMessage by mutableStateOf<String?>(null)


    val adminStats = MutableStateFlow<AppStatsDTO?>(null)

    fun fetchAdminStats() {
        viewModelScope.launch {
            try {
                val token = tokenProvider.getIdToken()
                adminStats.value = techRepository.getAppStats(token)
            } catch (e: Exception) {
                errorMessage = e.message
                println(errorMessage)
            }
        }
    }

    fun fetchTechnicians() {
        viewModelScope.launch {
            try {
                val technicians = techRepository.getTechnicians()
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
