package com.eygraber.seymour

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28])
actual class CommonSeymourTextUiTest : SeymourTextUiTest() {
  actual override fun SemanticsNodeInteraction.performButtonClick() = performClick()
}
