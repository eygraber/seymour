package com.eygraber.seymour

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
import androidx.compose.ui.text.style.TextDecoration
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

expect class CommonInlineSeymourTextUiTest : InlineSeymourTextUiTest {
  override fun SemanticsNodeInteraction.performLinkAnnotationClick(): SemanticsNodeInteraction
}

@OptIn(ExperimentalTestApi::class)
abstract class InlineSeymourTextUiTest {
  abstract fun SemanticsNodeInteraction.performLinkAnnotationClick(): SemanticsNodeInteraction

  @Test
  fun `test collapsed state`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }

      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a multiline text that\nshould be truncated.",
        seeMoreText = " Show More",
        seeMoreMaxLines = 1,
      )
    }

    onNodeWithText(" Show More", substring = true).assertExists()
  }

  @Test
  fun `test expanded state`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(true) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a non truncated text",
        seeMoreText = " Show More",
        seeMoreMaxLines = 1,
      )
    }

    onNodeWithText("This is a non truncated text").assertExists()
    onNodeWithText(" Show More", substring = true).assertDoesNotExist()
  }

  @Test
  fun `test expanded state with collapse text`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(true) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a non truncated text",
        seeMoreText = " Show More",
        seeLessText = " Show Less",
        seeMoreMaxLines = 1,
      )
    }

    onNodeWithText("This is a non truncated text Show Less").assertExists()
    onNodeWithText(" Show More", substring = true).assertDoesNotExist()
  }

  @Test
  fun `test expanded state with max expanded lines`() = runComposeUiTest {
    val text = "This is a truncated text\nthat should still\nhave an overflow when expanded"
    var hasVisualOverflow = false

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(true) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = text,
        seeMoreText = " Show More",
        seeMoreMaxLines = 1,
        seeLessMaxLines = 2,
        onTextLayout = {
          hasVisualOverflow = it.hasVisualOverflow
        },
      )
    }

    onNodeWithText(text).assertExists()

    onNodeWithText(" Show More", substring = true).assertDoesNotExist()

    assertTrue(hasVisualOverflow, "Text should have visual overflow")
  }

  @Test
  fun `test expand click`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a multiline text that\nshould be truncated.",
        seeMoreText = " Show More",
        seeMoreMaxLines = 1,
      )
    }

    onNodeWithText(" Show More", substring = true).performLinkAnnotationClick()

    onNodeWithText("This is a multiline text that\nshould be truncated.").assertExists()
    onNodeWithText(" Show More", substring = true).assertDoesNotExist()
  }

  @Test
  fun `test collapse click`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(true) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a multiline text that\nshould be truncated.",
        seeMoreText = " Show More",
        seeLessText = " Show Less",
        seeMoreMaxLines = 1,
      )
    }

    onNodeWithText(" Show Less", substring = true).performLinkAnnotationClick()

    onNodeWithText(" Show Less", substring = true).assertDoesNotExist()
    onNodeWithText(" Show More", substring = true).assertExists()
  }

  @Test
  fun `test expand click with max expanded lines`() = runComposeUiTest {
    val text = "This is a truncated text\nthat should still\nhave an overflow when expanded"
    var hasVisualOverflow = false

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = text,
        seeMoreText = " Show More",
        seeMoreMaxLines = 1,
        seeLessMaxLines = 2,
        onTextLayout = {
          hasVisualOverflow = it.hasVisualOverflow
        },
      )
    }

    onNodeWithText(" Show More", substring = true).performLinkAnnotationClick()

    onNodeWithText(" Show More", substring = true).assertDoesNotExist()
    onNodeWithText(" Show Less", substring = true).assertDoesNotExist()

    onNodeWithText(text).assertExists()

    assertTrue(hasVisualOverflow, "Text should have visual overflow")
  }

  @Test
  fun `test empty text`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "",
        seeMoreText = " Show More",
        seeMoreMaxLines = 1,
      )
    }

    onNodeWithText(" Show More", substring = true).assertDoesNotExist()
    onNodeWithText("").assertExists()
  }

  @Test
  fun `test single line text that fits within collapsed max lines`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "Short text",
        seeMoreText = " Show More",
        seeMoreMaxLines = 2,
      )
    }

    onNodeWithText("Short text").assertExists()
    onNodeWithText(" Show More", substring = true).assertDoesNotExist()
  }

  @Test
  fun `test text exactly at boundary of collapsed max lines`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "Line 1\nLine 2",
        seeMoreText = " More",
        seeMoreMaxLines = 2,
      )
    }

    onNodeWithText("Line 1\nLine 2").assertExists()
    onNodeWithText(" More", substring = true).assertDoesNotExist()
  }

  @Test
  fun `test text just over boundary of collapsed max lines`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "Line 1\nLine 2\nLine 3",
        seeMoreText = " Show More",
        seeMoreMaxLines = 2,
      )
    }

    onNodeWithText(" Show More", substring = true).assertExists()
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
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = annotatedText,
        seeMoreText = " Show More",
        seeMoreMaxLines = 1,
      )
    }

    onNodeWithText(" Show More", substring = true).assertExists()
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
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = annotatedText,
        seeMoreText = " Show More",
        seeMoreMaxLines = 1,
      )
    }

    onNodeWithText(" Show More", substring = true).performLinkAnnotationClick()
    onNodeWithText("This is bold text that\nshould be expanded.", substring = true).assertExists()
  }

  @Test
  fun `test custom see more and see less styles`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(true) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a long text that\nshould show custom styled links.",
        seeMoreText = " Read More",
        seeLessText = " Read Less",
        seeMoreStyle = SpanStyle(
          color = Color.Blue,
          textDecoration = TextDecoration.Underline,
        ),
        seeLessStyle = SpanStyle(
          color = Color.Red,
          fontWeight = FontWeight.Bold,
        ),
        seeMoreMaxLines = 1,
      )
    }

    onNodeWithText(" Read Less", substring = true).assertExists()
    onNodeWithText(" Read More", substring = true).assertDoesNotExist()

    onNodeWithText(" Read Less", substring = true).performLinkAnnotationClick()

    onNodeWithText(" Read More", substring = true).assertExists()
    onNodeWithText(" Read Less", substring = true).assertDoesNotExist()
  }

  @Test
  fun `test very large collapsed max lines`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "Line 1\nLine 2\nLine 3",
        seeMoreText = " Show More",
        seeMoreMaxLines = 100, // Much larger than needed
      )
    }

    onNodeWithText("Line 1\nLine 2\nLine 3").assertExists()
    onNodeWithText(" Show More", substring = true).assertDoesNotExist()
  }

  @Test
  fun `test onTextLayout callback is called`() = runComposeUiTest {
    var textLayoutCallCount = 0
    var lastTextLayoutResult: TextLayoutResult? = null

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is a test text\nwith multiple lines",
        seeMoreText = " Show More",
        seeMoreMaxLines = 1,
        onTextLayout = { result ->
          textLayoutCallCount++
          lastTextLayoutResult = result
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
        onSeeMoreChange = { isExpanded ->
          isSeeMoreExpanded = isExpanded
          isExternalState = isExpanded
        },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This text will change state\nexternally and internally.",
        seeMoreText = " Show More",
        seeLessText = " Show Less",
        seeMoreMaxLines = 1,
      )
    }

    // Initially collapsed
    onNodeWithText(" Show More", substring = true).assertExists()
    assertFalse(isExternalState)

    // Click to expand
    onNodeWithText(" Show More", substring = true).performLinkAnnotationClick()
    onNodeWithText(" Show Less", substring = true).assertExists()
    assertTrue(isExternalState)

    // Click to collapse
    onNodeWithText(" Show Less", substring = true).performLinkAnnotationClick()
    onNodeWithText(" Show More", substring = true).assertExists()
    assertFalse(isExternalState)
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
  fun `test null see less text behavior`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(true) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "This is expanded text\nwithout a collapse option.",
        seeMoreText = " Show More",
        seeLessText = null, // Explicitly null
        seeMoreMaxLines = 1,
      )
    }

    onNodeWithText("This is expanded text\nwithout a collapse option.").assertExists()
    onNodeWithText(" Show More", substring = true).assertDoesNotExist()
    onNodeWithText("Show Less", substring = true).assertDoesNotExist()
    onNodeWithText("Less", substring = true).assertDoesNotExist()
  }

  @Test
  fun `test edge case with single character lines`() = runComposeUiTest {
    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "A\nB\nC\nD",
        seeMoreText = " More",
        seeMoreMaxLines = 2,
      )
    }

    onNodeWithText(" More", substring = true).assertExists()

    onNodeWithText(" More", substring = true).performLinkAnnotationClick()
    onNodeWithText("A\nB\nC\nD").assertExists()
  }

  @Test
  fun `test line count accuracy`() = runComposeUiTest {
    var lineCount = 0

    setContent {
      var isSeeMoreExpanded by remember { mutableStateOf(false) }
      SeymourText(
        onSeeMoreChange = { isSeeMoreExpanded = it },
        isSeeMoreExpanded = isSeeMoreExpanded,
        text = "Line 1\nLine 2\nLine 3\nLine 4",
        seeMoreText = " Show More",
        seeMoreMaxLines = 2,
        onTextLayout = { result ->
          lineCount = result.lineCount
        },
      )
    }

    // In collapsed state, should be truncated to seeMoreMaxLines
    assertEquals(2, lineCount, "Line count should equal seeMoreMaxLines when truncated")
  }
}
