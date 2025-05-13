package org.guivicj.support.ui.screens.tickets.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.guivicj.support.domain.model.MessageDTO

@Composable
fun MessageBubble(message: MessageDTO, isCurrentUser: Boolean, grouped: Boolean) {
    val topPadding = if (grouped) 4.dp else 0.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topPadding, bottom = 4.dp, start = 12.dp, end = 12.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color =
                        if (isCurrentUser) MaterialTheme.colorScheme.outline
                        else MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(
                        topStart = if (isCurrentUser) 16.dp else 0.dp,
                        topEnd = if (isCurrentUser) 0.dp else 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = message.content,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = if (isCurrentUser) TextAlign.End else TextAlign.Start
            )
        }
    }
}
