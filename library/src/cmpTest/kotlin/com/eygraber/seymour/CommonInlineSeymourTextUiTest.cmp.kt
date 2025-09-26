@file:Suppress("ktlint:standard:max-line-length")

package com.eygraber.seymour

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.Density
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
actual class CommonInlineSeymourTextUiTest : InlineSeymourTextUiTest() {
  actual override fun SemanticsNodeInteraction.performLinkAnnotationClick() =
    onChildren()[0].performClick()

  @Test
  fun `CMP test very long single line text`() = runComposeUiTest {
    val longText =
      "This is a very long single line of text that should definitely be truncated because it exceeds the normal line width and would cause visual overflow in the component when displayed with a single line limit."

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = longText,
        seeMoreText = " More",
        seeMoreMaxLines = 1,
      )
    }

    onNodeWithText(" More", substring = true).assertExists()

    // Expand and verify full text is shown
    onNodeWithText(" More", substring = true).performLinkAnnotationClick()
    onNodeWithText(" More", substring = true).assertDoesNotExist()
    onNodeWithText(longText, substring = true).assertExists()
  }

  @Test
  fun `test behavior with different density values`() = runComposeUiTest {
    var layoutCallCount = 0

    setContent {
      val customDensity = Density(2.0F, 1.0F)
      CompositionLocalProvider(LocalDensity provides customDensity) {
        var isSeeMoreExpanded by remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = { isSeeMoreExpanded = it },
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This is a long text that should be truncated at higher density\nbecause it takes up more pixels per dp",
          seeMoreText = " Show More",
          seeMoreMaxLines = 1,
          onTextLayout = { result ->
            layoutCallCount++
          },
        )
      }
    }

    assertTrue(layoutCallCount > 0, "Layout should be called at least once")
    onNodeWithText(" Show More", substring = true).assertExists()

    // Expand the text
    onNodeWithText(" Show More", substring = true).performLinkAnnotationClick()
    onNodeWithText(
      "This is a long text that should be truncated at higher density\nbecause it takes up more pixels per dp",
    ).assertExists()
  }

  @Test
  fun `test density change triggers recomposition`() = runComposeUiTest {
    var currentDensity by mutableStateOf(Density(1.0F, 1.0F))
    var layoutCallCount = 0

    setContent {
      CompositionLocalProvider(LocalDensity provides currentDensity) {
        var isSeeMoreExpanded by remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = { isSeeMoreExpanded = it },
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This text behavior should change when density changes because text measurement is density-dependent",
          seeMoreText = " More",
          seeMoreMaxLines = 1,
          onTextLayout = { layoutCallCount++ },
        )
      }
    }

    val initialLayoutCount = layoutCallCount

    // Change density
    runOnIdle {
      currentDensity = Density(2.0F, 1.0F)
    }

    waitForIdle()

    // Verify layout was recalculated
    assertTrue(
      layoutCallCount > initialLayoutCount,
      "Layout should be recalculated when density changes",
    )
  }

  @Test
  fun `test truncation consistency across density changes`() = runComposeUiTest {
    val text = "This is a moderately long text that may or may not be truncated depending on the density settings"
    var currentDensity by mutableStateOf(Density(0.75F, 1.0F))
    var hasTruncationAtHighDensity = false

    setContent {
      CompositionLocalProvider(LocalDensity provides currentDensity) {
        var isSeeMoreExpanded by remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = { isSeeMoreExpanded = it },
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = text,
          seeMoreText = " More",
          seeMoreMaxLines = 2,
          onTextLayout = { result ->
            if(currentDensity.density == 4F) {
              hasTruncationAtHighDensity = result.hasVisualOverflow
            }
          },
        )
      }
    }

    waitForIdle()

    // Change to higher density
    currentDensity = Density(4.0F, 1.0F)

    waitForIdle()

    // At higher density, text measurements are larger, so truncation is more likely
    // This test ensures our component handles density changes correctly
    assertTrue(
      hasTruncationAtHighDensity,
      "Text should be truncated at high density due to larger measurements",
    )
  }

  @Test
  fun `test see more link visibility with density changes`() = runComposeUiTest {
    val mediumText =
      "This text length is borderline and may\nshow different behavior at different densities"
    var currentDensity by mutableStateOf(Density(1.0F, 1.0F))

    setContent {
      CompositionLocalProvider(LocalDensity provides currentDensity) {
        var isSeeMoreExpanded by remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = { isSeeMoreExpanded = it },
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = mediumText,
          seeMoreText = " Show More",
          seeMoreMaxLines = 1,
        )
      }
    }

    // At normal density, check if see more link exists
    onNodeWithText(" Show More", substring = true).assertExists()

    // Change to higher density
    runOnIdle {
      currentDensity = Density(2.5F, 1.0F)
    }

    waitForIdle()

    // At higher density, see more link should be present due to larger text measurements
    onNodeWithText(" Show More", substring = true).assertExists()
  }

  @Test
  fun `test font scale affects text measurement and truncation`() = runComposeUiTest {
    val text = "This text should be affected by font scale changes which impact text measurement"
    var currentDensity by mutableStateOf(Density(1.0F, 1.0F))
    var textWidthAtNormalScale = 0
    var textWidthAtLargeScale = 0

    setContent {
      CompositionLocalProvider(LocalDensity provides currentDensity) {
        var isSeeMoreExpanded by remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = { isSeeMoreExpanded = it },
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = text,
          seeMoreText = " More",
          seeMoreMaxLines = 1,
          onTextLayout = { result ->
            if(currentDensity.fontScale == 1.0F) {
              textWidthAtNormalScale = result.size.width
            }
            else {
              textWidthAtLargeScale = result.size.width
            }
          },
        )
      }
    }

    waitForIdle()

    // Change to larger font scale (simulating accessibility settings)
    currentDensity = Density(1.0F, 1.5F)

    waitForIdle()

    assertTrue(
      textWidthAtLargeScale > textWidthAtNormalScale,
      "Text should be wider with larger font scale",
    )
  }

  @Test
  fun `test expanded max lines behavior with density changes`() = runComposeUiTest {
    val longText =
      "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nThis is a very long text that spans multiple lines"
    var currentDensity by mutableStateOf(Density(1.0F, 1.0F))
    var hasOverflowAtLowDensity = false
    var hasOverflowAtHighDensity = false

    setContent {
      CompositionLocalProvider(LocalDensity provides currentDensity) {
        var isSeeMoreExpanded by remember { mutableStateOf(true) }
        SeymourText(
          onSeeMoreChange = { isSeeMoreExpanded = it },
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = longText,
          seeMoreText = " More",
          seeLessText = " Less",
          seeMoreMaxLines = 2,
          seeLessMaxLines = 3,
          onTextLayout = { result ->
            if(currentDensity.density == 1.0F) {
              hasOverflowAtLowDensity = result.hasVisualOverflow
            }
            else {
              hasOverflowAtHighDensity = result.hasVisualOverflow
            }
          },
        )
      }
    }

    waitForIdle()

    // Change to high density
    runOnIdle {
      currentDensity = Density(2.5F, 1.0F)
    }

    waitForIdle()

    // Both should have overflow since we have more than 3 lines of content
    assertTrue(hasOverflowAtLowDensity, "Should have overflow at low density")
    assertTrue(hasOverflowAtHighDensity, "Should have overflow at high density")
  }

  @Test
  fun `test state preservation across density changes`() = runComposeUiTest {
    var currentDensity by mutableStateOf(Density(1.0F, 1.0F))
    var isExpanded by mutableStateOf(false)

    setContent {
      CompositionLocalProvider(LocalDensity provides currentDensity) {
        SeymourText(
          onSeeMoreChange = { isExpanded = it },
          isSeeMoreExpanded = isExpanded,
          text = "This is a very long text that should definitely maintain its expanded state even when\ndensity changes affect measurement and text layout calculations significantly across different device configurations",
          seeMoreText = " Expand",
          seeLessText = " Collapse",
          seeMoreMaxLines = 1,
        )
      }
    }

    // Initially collapsed - verify the text is long enough to be truncated
    onNodeWithText(" Expand", substring = true).assertExists()
    assertFalse(isExpanded)

    // Expand the text
    onNodeWithText(" Expand", substring = true).performLinkAnnotationClick()

    // Wait for state to settle before checking
    waitForIdle()

    assertTrue(isExpanded)
    onNodeWithText(" Collapse", substring = true).assertExists()

    // Change density while expanded
    runOnIdle {
      currentDensity = Density(2.0F, 1.0F)
    }

    waitForIdle()

    // State should be preserved
    assertTrue(isExpanded, "Expanded state should be preserved across density changes")
    onNodeWithText(" Collapse", substring = true).assertExists()

    // Should still be able to collapse
    onNodeWithText(" Collapse", substring = true).performLinkAnnotationClick()
    waitForIdle()
    assertFalse(isExpanded)
    onNodeWithText(" Expand", substring = true).assertExists()
  }
}
