package org.guivicj.support.ui.screens.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import support_mobile.composeapp.generated.resources.Res
import support_mobile.composeapp.generated.resources.search_placeholder

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(stringResource(Res.string.search_placeholder)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
        ),
        textStyle = MaterialTheme.typography.bodyMedium,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}
