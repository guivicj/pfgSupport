package org.guivicj.support.ui.screens.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Briefcase
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Hourglass
import com.composables.icons.lucide.Lucide
import org.guivicj.support.domain.model.TechStatsDTO
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.stats_assigned
import support_mobile.composeapp.generated.resources.stats_closed
import support_mobile.composeapp.generated.resources.stats_in_progress
import support_mobile.composeapp.generated.resources.stats_performance

@Composable
fun StatsContent(stats: TechStatsDTO, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        StatCard(
            label = stringResource(Res.string.stats_assigned),
            value = stats.totalTickets.toString(),
            icon = Lucide.Briefcase,
            color = Color(0xFF4285F4)
        )

        StatCard(
            label = stringResource(Res.string.stats_closed),
            value = stats.totalResolved.toString(),
            icon = Lucide.CircleCheck,
            color = Color(0xFF0F9D58)
        )

        StatCard(
            label = stringResource(Res.string.stats_in_progress),
            value = stats.totalInProgress.toString(),
            icon = Lucide.Hourglass,
            color = Color(0xFFFBBC05)
        )

        StatCard(
            label = stringResource(Res.string.stats_performance),
            value = stats.avgResolutionTime,
            icon = Lucide.Clock,
            color = Color(0xFF673AB7)
        )
    }
}
