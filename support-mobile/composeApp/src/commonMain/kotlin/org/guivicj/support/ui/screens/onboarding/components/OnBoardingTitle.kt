package org.guivicj.support.ui.screens.onboarding.components

import androidx.compose.runtime.Composable
import org.guivicj.support.ui.core.components.texts.SubtitleText
import org.guivicj.support.ui.core.components.texts.TitleText


@Composable
fun OnBoardingTitle(title: String, subtitle: String) {
    TitleText(title)
    SubtitleText(subtitle)
}