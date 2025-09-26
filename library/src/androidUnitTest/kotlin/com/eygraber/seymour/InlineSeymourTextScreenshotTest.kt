@file:Suppress("ktlint:standard:max-line-length")

package com.eygraber.seymour

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class InlineSeymourTextScreenshotTest {
  @get:Rule
  val paparazzi = Paparazzi()

  @Test
  fun inlineSeymourText_basic_collapsed_state() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This is a long multiline text that should be truncated in collapsed state and show the see more link.",
          seeMoreText = " See More",
          seeMoreMaxLines = 2,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_basic_expanded_state() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(true) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This is a long multiline text that should be fully visible in expanded state without any truncation.",
          seeMoreText = " See More",
          seeMoreMaxLines = 2,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_expanded_with_see_less() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(true) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This is a long multiline text that should show a see less link when expanded and seeLessText is provided.",
          seeMoreText = " See More",
          seeLessText = " See Less",
          seeMoreMaxLines = 2,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_short_text_no_truncation() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Short text",
          seeMoreText = " See More",
          seeMoreMaxLines = 3,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_single_line_collapsed() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This is a very long single line of text that should be truncated when collapsed to one line and show the see more link at the end",
          seeMoreText = " More",
          seeMoreMaxLines = 1,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_annotated_string_collapsed() {
    paparazzi.snapshot {
      TestContainer {
        val annotatedText = AnnotatedString.Builder().apply {
          append("This text has ")
          pushStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue))
          append("bold blue")
          pop()
          append(" and ")
          pushStyle(SpanStyle(fontStyle = FontStyle.Italic, color = Color.Red))
          append("italic red")
          pop()
          append(" styled content that should be truncated.")
        }.toAnnotatedString()

        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = annotatedText,
          seeMoreText = " Read More",
          seeMoreMaxLines = 1,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_annotated_string_expanded() {
    paparazzi.snapshot {
      TestContainer {
        val annotatedText = AnnotatedString.Builder().apply {
          append("This text has ")
          pushStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue))
          append("bold blue")
          pop()
          append(" and ")
          pushStyle(SpanStyle(fontStyle = FontStyle.Italic, color = Color.Red))
          append("italic red")
          pop()
          append(" styled content that should be fully visible when expanded.")
        }.toAnnotatedString()

        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(true) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = annotatedText,
          seeMoreText = " Read More",
          seeLessText = " Read Less",
          seeMoreMaxLines = 1,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_custom_link_styles() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This text demonstrates custom styling for the see more and see less links with different colors and decorations.",
          seeMoreText = " 📖 Read More",
          seeMoreStyle = SpanStyle(
            color = Color(0xFF2196F3),
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold,
          ),
          seeMoreMaxLines = 2,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_custom_link_styles_expanded() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(true) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This text demonstrates custom styling for the see more and see less links with different colors and decorations.",
          seeMoreText = " 📖 Read More",
          seeLessText = " 📕 Read Less",
          seeMoreStyle = SpanStyle(
            color = Color(0xFF2196F3),
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold,
          ),
          seeLessStyle = SpanStyle(
            color = Color(0xFFE91E63),
            textDecoration = TextDecoration.LineThrough,
            fontStyle = FontStyle.Italic,
          ),
          seeMoreMaxLines = 2,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_different_text_styles() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This text uses a larger font size and different styling to test how SeymourText handles various text styles and sizing.",
          seeMoreText = " Continue",
          style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF424242),
          ),
          seeMoreMaxLines = 2,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_empty_text() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "",
          seeMoreText = " See More",
          seeMoreMaxLines = 2,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_exactly_at_line_boundary() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Line one\nLine two", // Exactly 2 lines
          seeMoreText = " More",
          seeMoreMaxLines = 2,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_just_over_line_boundary() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Line one\nLine two\nLine three", // 3 lines, one over limit
          seeMoreText = " More",
          seeMoreMaxLines = 2,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_many_lines_collapsed() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10",
          seeMoreText = " Show All",
          seeMoreMaxLines = 3,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_many_lines_expanded() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(true) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10",
          seeMoreText = " Show All",
          seeLessText = " Show Less",
          seeMoreMaxLines = 3,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_limited_expanded_max_lines() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(true) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8",
          seeMoreText = " Expand",
          seeLessText = " Collapse",
          seeMoreMaxLines = 2,
          seeLessMaxLines = 5, // Still truncated when expanded
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_very_long_single_word() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Supercalifragilisticexpialidocious is a very long word that might cause interesting wrapping behavior when truncated",
          seeMoreText = " Continue Reading",
          seeMoreMaxLines = 1,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_mixed_content_with_numbers_and_symbols() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Price: $29.99 📱 Available in colors: Red, Blue, Green, Yellow, Purple, Orange, Pink, Black, White, Gray, Brown 🎨 Rating: 4.8/5 ⭐ Ships in 2-3 business days 🚚",
          seeMoreText = " See Details",
          seeMoreMaxLines = 2,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_narrow_width_container() {
    paparazzi.snapshot {
      TestContainer {
        Box(modifier = Modifier.width(200.dp)) {
          val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
          SeymourText(
            onSeeMoreChange = setIsSeeMoreExpanded,
            isSeeMoreExpanded = isSeeMoreExpanded,
            text = "This is a test of how SeymourText behaves in a narrow container where text wrapping is more aggressive.",
            seeMoreText = " More",
            seeMoreMaxLines = 3,
          )
        }
      }
    }
  }

  @Test
  fun inlineSeymourText_single_character_lines() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "A\nB\nC\nD\nE\nF\nG",
          seeMoreText = " More",
          seeMoreMaxLines = 3,
        )
      }
    }
  }

  @Test
  fun inlineSeymourText_special_unicode_characters() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          onSeeMoreChange = setIsSeeMoreExpanded,
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Unicode test: 🌍 Hello 世界 🌟 Здравствуй мир 🎉 مرحبا بالعالم 🌺 This tests international characters and emojis",
          seeMoreText = " Show More",
          seeMoreMaxLines = 1,
        )
      }
    }
  }

  @Composable
  private fun TestContainer(content: @Composable () -> Unit) {
    MaterialTheme {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
      ) {
        Box(modifier = Modifier.fillMaxWidth()) {
          content()
        }
      }
    }
  }
}
