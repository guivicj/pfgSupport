package org.guivicj.support.ui.core.components.display

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        singleLine = true,
        supportingText = {
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        ),
    )
}