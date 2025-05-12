package org.guivicj.support.ui.core.components.texts

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TitleText(text: String, modifier: Modifier? = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier ?: Modifier.padding(bottom = 12.dp)
    )
}