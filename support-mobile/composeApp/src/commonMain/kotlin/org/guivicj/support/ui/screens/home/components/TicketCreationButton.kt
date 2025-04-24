package org.guivicj.support.ui.screens.home.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TicketCreationButton(label: String, onClick: () -> Unit, modifier: Modifier) {
    OutlinedButton(
        modifier = modifier
            .height(50.dp)
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.outline,
            contentColor = MaterialTheme.colorScheme.background
        ),
        onClick = onClick
    ) {
        Text(text = label)
    }
}