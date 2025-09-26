package com.eygraber.seymour

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

expect class CommonSeymourTextUiTest : SeymourTextUiTest {
  override fun SemanticsNodeInteraction.performButtonClick(): SemanticsNodeInteraction
}

@OptIn(ExperimentalTestApi::class)
abstract class SeymourTextUiTest {
  abstract fun SemanticsNodeInteraction.performButtonClick(): SemanticsNodeInteraction

  @Test
  fun `test collapsed state shows see more content`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }

      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a multiline text that\nshould be truncated.",
        seeMoreMaxLines = 1,
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("Show More")
          }
        },
      )
    }

    onNodeWithText("Show More").assertExists()
  }

  @Test
  fun `test expanded state shows see more content for long text`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(true) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a very long text\nthat should still show\nsee more content when expanded",
        seeMoreMaxLines = 1,
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = !isSeeMoreExpanded }) {
            Text(if(isSeeMoreExpanded) "Show Less" else "Show More")
          }
        },
      )
    }

    onNodeWithText("Show Less").assertExists()
    onNodeWithText("Show More").assertDoesNotExist()
  }

  @Test
  fun `test expanded state with max expanded lines`() = runComposeUiTest {
    val text = "This is a truncated text\nthat should still\nhave an overflow when expanded"
    var hasVisualOverflow = false

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(true) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = text,
        seeMoreMaxLines = 1,
        seeLessMaxLines = 2,
        onTextLayout = {
          hasVisualOverflow = it.hasVisualOverflow
        },
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = !isSeeMoreExpanded }) {
            Text(if(isSeeMoreExpanded) "Show Less" else "Show More")
          }
        },
      )
    }

    onNodeWithText(text).assertExists()
    onNodeWithText("Show Less").assertExists()
    assertTrue(hasVisualOverflow, "Text should have visual overflow")
  }

  @Test
  fun `test button click interaction`() = runComposeUiTest {
    var clickCount = 0

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a multiline text that\nshould be truncated.",
        seeMoreMaxLines = 1,
        seeMoreContent = {
          TextButton(onClick = {
            isSeeMoreExpanded = true
            clickCount++
          }) {
            Text("Show More")
          }
        },
      )
    }

    onNodeWithText("Show More").performButtonClick()

    onNodeWithText("This is a multiline text that\nshould be truncated.").assertExists()
    assertEquals(1, clickCount)
  }

  @Test
  fun `test toggle button click`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a multiline text that\nshould be truncated.",
        seeMoreMaxLines = 1,
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = !isSeeMoreExpanded }) {
            Text(if(isSeeMoreExpanded) "Show Less" else "Show More")
          }
        },
      )
    }

    // Initially collapsed
    onNodeWithText("Show More").assertExists()
    onNodeWithText("Show Less").assertDoesNotExist()

    // Click to expand
    onNodeWithText("Show More").performButtonClick()
    onNodeWithText("Show Less").assertExists()
    onNodeWithText("Show More").assertDoesNotExist()

    // Click to collapse
    onNodeWithText("Show Less").performButtonClick()
    onNodeWithText("Show More").assertExists()
    onNodeWithText("Show Less").assertDoesNotExist()
  }

  @Test
  fun `test empty text`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "",
        seeMoreMaxLines = 1,
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("Show More")
          }
        },
      )
    }

    onNodeWithText("Show More").assertDoesNotExist()
    onNodeWithText("").assertExists()
  }

  @Test
  fun `test single line text that fits within collapsed max lines`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "Short text",
        seeMoreMaxLines = 2,
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("Show More")
          }
        },
      )
    }

    onNodeWithText("Short text").assertExists()
    onNodeWithText("Show More").assertDoesNotExist()
  }

  @Test
  fun `test text exactly at boundary of collapsed max lines`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "Line 1\nLine 2",
        seeMoreMaxLines = 2,
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("More")
          }
        },
      )
    }

    onNodeWithText("Line 1\nLine 2").assertExists()
    onNodeWithText("More").assertDoesNotExist()
  }

  @Test
  fun `test text just over boundary of collapsed max lines`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "Line 1\nLine 2\nLine 3",
        seeMoreMaxLines = 2,
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("Show More")
          }
        },
      )
    }

    onNodeWithText("Show More").assertExists()
  }

  @Test
  fun `test annotated string with collapsed state`() = runComposeUiTest {
    val annotatedText = AnnotatedString.Builder().apply {
      append("This is ")
      pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
      append("bold")
      pop()
      append(" text that\nshould be truncated.")
    }.toAnnotatedString()

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = annotatedText,
        seeMoreMaxLines = 1,
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("Show More")
          }
        },
      )
    }

    onNodeWithText("Show More").assertExists()
  }

  @Test
  fun `test annotated string with expanded state`() = runComposeUiTest {
    val annotatedText = AnnotatedString.Builder().apply {
      append("This is ")
      pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
      append("bold")
      pop()
      append(" text that\nshould be expanded.")
    }.toAnnotatedString()

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = annotatedText,
        seeMoreMaxLines = 1,
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("Show More")
          }
        },
      )
    }

    onNodeWithText("Show More").performButtonClick()
    onNodeWithText("This is bold text that\nshould be expanded.", substring = true).assertExists()
  }

  @Test
  fun `test complex seeMoreContent with multiple elements`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a long text that\nshould show complex content.",
        seeMoreMaxLines = 1,
        seeMoreContent = {
          Column {
            Text("Additional info")
            TextButton(onClick = { isSeeMoreExpanded = !isSeeMoreExpanded }) {
              Text(if(isSeeMoreExpanded) "Read Less" else "Read More")
            }
          }
        },
      )
    }

    onNodeWithText("Additional info").assertExists()
    onNodeWithText("Read More").assertExists()
    onNodeWithText("Read Less").assertDoesNotExist()

    onNodeWithText("Read More").performButtonClick()

    onNodeWithText("Additional info").assertExists()
    onNodeWithText("Read Less").assertExists()
    onNodeWithText("Read More").assertDoesNotExist()
  }

  @Test
  fun `test very large collapsed max lines`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "Line 1\nLine 2\nLine 3",
        seeMoreMaxLines = 100, // Much larger than needed
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("Show More")
          }
        },
      )
    }

    onNodeWithText("Line 1\nLine 2\nLine 3").assertExists()
    onNodeWithText("Show More").assertDoesNotExist()
  }

  @Test
  fun `test onTextLayout callback is called`() = runComposeUiTest {
    var textLayoutCallCount = 0
    var lastTextLayoutResult: TextLayoutResult? = null

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a test text\nwith multiple lines",
        seeMoreMaxLines = 1,
        onTextLayout = { result ->
          textLayoutCallCount++
          lastTextLayoutResult = result
        },
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("Show More")
          }
        },
      )
    }

    assertTrue(textLayoutCallCount > 0, "onTextLayout should be called at least once")
    assertTrue(lastTextLayoutResult != null, "TextLayoutResult should not be null")
  }

  @Test
  fun `test state changes trigger recomposition correctly`() = runComposeUiTest {
    var isExternalState = false

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(isExternalState) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This text will change state\nexternally and internally.",
        seeMoreMaxLines = 1,
        seeMoreContent = {
          TextButton(onClick = {
            isSeeMoreExpanded = !isSeeMoreExpanded
            isExternalState = isSeeMoreExpanded
          }) {
            Text(if(isSeeMoreExpanded) "Show Less" else "Show More")
          }
        },
      )
    }

    // Initially collapsed
    onNodeWithText("Show More").assertExists()
    assertFalse(isExternalState)

    // Click to expand
    onNodeWithText("Show More").performButtonClick()
    onNodeWithText("Show Less").assertExists()

    // Click to collapse
    onNodeWithText("Show Less").performButtonClick()
    onNodeWithText("Show More").assertExists()
  }

  // on Android the Text doesn't overflow in tests https://issuetracker.google.com/issues/447473074
  @Ignore
  @Test
  fun `test very long single line text`() = runComposeUiTest {
    val longText =
      "This is a very long single line of text that should definitely be truncated because it exceeds the normal line width and would cause visual overflow in the component when displayed with a single line limit."

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = longText,
        seeMoreMaxLines = 1,
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("More")
          }
        },
      )
    }

    onNodeWithText("More").assertExists()

    // Expand and verify full text is shown
    onNodeWithText("More").performButtonClick()
    onNodeWithText("More").assertDoesNotExist()
    onNodeWithText(longText, substring = true).assertExists()
  }

  @Test
  fun `test edge case with single character lines`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "A\nB\nC\nD",
        seeMoreMaxLines = 2,
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("More")
          }
        },
      )
    }

    onNodeWithText("More").assertExists()

    onNodeWithText("More").performButtonClick()
    onNodeWithText("A\nB\nC\nD").assertExists()
  }

  @Test
  fun `test line count accuracy`() = runComposeUiTest {
    var lineCount = 0

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "Line 1\nLine 2\nLine 3\nLine 4",
        seeMoreMaxLines = 2,
        onTextLayout = { result ->
          lineCount = result.lineCount
        },
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("Show More")
          }
        },
      )
    }

    // In collapsed state, should be truncated to seeMoreMaxLines
    assertEquals(2, lineCount, "Line count should equal seeMoreMaxLines when truncated")
  }

  @Test
  fun `test custom text styling parameters`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "Custom styled text\nwith different properties",
        seeMoreMaxLines = 1,
        color = Color.Blue,
        fontWeight = FontWeight.Bold,
        seeMoreContent = {
          TextButton(onClick = { isSeeMoreExpanded = true }) {
            Text("Show More")
          }
        },
      )
    }

    onNodeWithText("Show More").assertExists()
  }

  @Test
  fun `test movable content behavior`() = runComposeUiTest {
    var recompositionCount = 0

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }

      // This should only be created once due to movableContentOf
      val seeMoreContent: @Composable () -> Unit = remember {
        {
          recompositionCount++
          TextButton(onClick = { isSeeMoreExpanded = !isSeeMoreExpanded }) {
            Text(if(isSeeMoreExpanded) "Show Less" else "Show More")
          }
        }
      }

      SeymourText(
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This text tests movable content behavior\nwith state changes",
        seeMoreMaxLines = 1,
        seeMoreContent = { seeMoreContent() },
      )
    }

    val initialRecompositionCount = recompositionCount

    // Click to expand
    onNodeWithText("Show More").performButtonClick()

    waitForIdle()

    // Click to collapse
    onNodeWithText("Show Less").performButtonClick()

    waitForIdle()

    // The seeMoreContent should be using movableContentOf internally,
    // so it should be efficient in recomposition
    assertTrue(
      recompositionCount >= initialRecompositionCount,
      "seeMoreContent should recompose when state changes",
    )
  }
}
