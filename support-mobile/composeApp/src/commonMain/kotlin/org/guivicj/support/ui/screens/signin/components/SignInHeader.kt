package org.guivicj.support.ui.screens.signin.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.guivicj.support.ui.core.components.GradientTopBackground
import org.guivicj.support.ui.core.components.SubtitleText
import org.guivicj.support.ui.core.components.TitleText

@Composable
fun SignInHeader(
    title: String,
    subTitle: String,
    modifier: Modifier = Modifier,
) {
    GradientTopBackground {
        Column(modifier = modifier.padding(top = 50.dp)) {
            TitleText(title)
            SubtitleText(subTitle)
        }
    }
}