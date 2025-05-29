package org.guivicj.support.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.guivicj.support.domain.repository.TechRepository
import org.guivicj.support.state.TechAnalyticsState

class TechAnalyticsViewModel(
    private val techRepository: TechRepository
) : ViewModel() {
    var state by mutableStateOf<TechAnalyticsState>(TechAnalyticsState.Loading)
        private set

    fun loadStats(techId: Long) {
        viewModelScope.launch {
            state = TechAnalyticsState.Loading
            val result = techRepository.getTechStats(techId)
            println(result)
            result
                .onSuccess { stats ->
                    state = TechAnalyticsState.Success(stats)
                }
                .onFailure {
                    state = TechAnalyticsState.Error("Failed to load stats")
                }
        }
    }
}