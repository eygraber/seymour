@file:Suppress("ktlint:standard:max-line-length")

package com.eygraber.seymour

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
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
import kotlin.test.Ignore

@Ignore
class SeymourTextScreenshotTest {
  @get:Rule
  val paparazzi = Paparazzi()

  @Test
  fun seymourText_basic_collapsed_state() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This is a long multiline text that should be truncated in collapsed state and show the see more content.",
          seeMoreMaxLines = 2,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("See More")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_basic_expanded_state() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(true) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This is a long multiline text that should be fully visible in expanded state without any truncation.",
          seeMoreMaxLines = 2,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(false) }) {
              Text(if(isSeeMoreExpanded) "See Less" else "See More")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_expanded_with_see_less() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(true) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This is a long multiline text that should show a see less button when expanded and appropriate seeMoreContent is provided.",
          seeMoreMaxLines = 2,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(!isSeeMoreExpanded) }) {
              Text(if(isSeeMoreExpanded) "See Less" else "See More")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_short_text_no_truncation() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Short text",
          seeMoreMaxLines = 3,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("See More")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_single_line_collapsed() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This is a very long single line of text that should be truncated when collapsed to one line and show the see more content at the end",
          seeMoreMaxLines = 1,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("More")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_annotated_string_collapsed() {
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
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = annotatedText,
          seeMoreMaxLines = 1,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("Read More")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_annotated_string_expanded() {
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
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = annotatedText,
          seeMoreMaxLines = 1,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(!isSeeMoreExpanded) }) {
              Text(if(isSeeMoreExpanded) "Read Less" else "Read More")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_custom_button_styles() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This text demonstrates custom styling for the see more and see less content with different button styles and colors.",
          seeMoreMaxLines = 2,
          seeMoreContent = {
            TextButton(
              onClick = { setIsSeeMoreExpanded(!isSeeMoreExpanded) },
            ) {
              Text(
                text = if(isSeeMoreExpanded) "📕 Read Less" else "📖 Read More",
                style = TextStyle(
                  color = Color(0xFF2196F3),
                  textDecoration = TextDecoration.Underline,
                  fontWeight = FontWeight.Bold,
                ),
              )
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_custom_button_styles_expanded() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(true) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This text demonstrates custom styling for the see more and see less content with different button styles and colors.",
          seeMoreMaxLines = 2,
          seeMoreContent = {
            TextButton(
              onClick = { setIsSeeMoreExpanded(!isSeeMoreExpanded) },
            ) {
              Text(
                text = if(isSeeMoreExpanded) "📕 Read Less" else "📖 Read More",
                style = TextStyle(
                  color = if(isSeeMoreExpanded) Color(0xFFE91E63) else Color(0xFF2196F3),
                  textDecoration = if(isSeeMoreExpanded) TextDecoration.LineThrough else TextDecoration.Underline,
                  fontWeight = FontWeight.Bold,
                  fontStyle = if(isSeeMoreExpanded) FontStyle.Italic else FontStyle.Normal,
                ),
              )
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_different_text_styles() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This text uses a larger font size and different styling to test how SeymourText handles various text styles and sizing.",
          seeMoreMaxLines = 2,
          style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF424242),
          ),
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("Continue")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_empty_text() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "",
          seeMoreMaxLines = 2,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("See More")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_exactly_at_line_boundary() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Line one\nLine two", // Exactly 2 lines
          seeMoreMaxLines = 2,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("More")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_just_over_line_boundary() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Line one\nLine two\nLine three", // 3 lines, one over limit
          seeMoreMaxLines = 2,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("More")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_many_lines_collapsed() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10",
          seeMoreMaxLines = 3,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("Show All")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_many_lines_expanded() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(true) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10",
          seeMoreMaxLines = 3,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(!isSeeMoreExpanded) }) {
              Text(if(isSeeMoreExpanded) "Show Less" else "Show All")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_limited_expanded_max_lines() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(true) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8",
          seeMoreMaxLines = 2,
          seeLessMaxLines = 5, // Still truncated when expanded
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(!isSeeMoreExpanded) }) {
              Text(if(isSeeMoreExpanded) "Collapse" else "Expand")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_very_long_single_word() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Supercalifragilisticexpialidocious is a very long word that might cause interesting wrapping behavior when truncated",
          seeMoreMaxLines = 1,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("Continue Reading")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_mixed_content_with_numbers_and_symbols() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Price: $29.99 📱 Available in colors: Red, Blue, Green, Yellow, Purple, Orange, Pink, Black, White, Gray, Brown 🎨 Rating: 4.8/5 ⭐ Ships in 2-3 business days 🚚",
          seeMoreMaxLines = 2,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("See Details")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_narrow_width_container() {
    paparazzi.snapshot {
      TestContainer {
        Box(modifier = Modifier.width(200.dp)) {
          val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
          SeymourText(
            isSeeMoreExpanded = isSeeMoreExpanded,
            text = "This is a test of how SeymourText behaves in a narrow container where text wrapping is more aggressive.",
            seeMoreMaxLines = 3,
            seeMoreContent = {
              TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
                Text("More")
              }
            },
          )
        }
      }
    }
  }

  @Test
  fun seymourText_single_character_lines() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "A\nB\nC\nD\nE\nF\nG",
          seeMoreMaxLines = 3,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("More")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_special_unicode_characters() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "Unicode test: 🌍 Hello 世界 🌟 Здравствуй мир 🎉 مرحبا بالعالم 🌺 This tests international characters and emojis",
          seeMoreMaxLines = 1,
          seeMoreContent = {
            TextButton(onClick = { setIsSeeMoreExpanded(true) }) {
              Text("Show More")
            }
          },
        )
      }
    }
  }

  @Test
  fun seymourText_complex_seeMoreContent() {
    paparazzi.snapshot {
      TestContainer {
        val (isSeeMoreExpanded, setIsSeeMoreExpanded) = remember { mutableStateOf(false) }
        SeymourText(
          isSeeMoreExpanded = isSeeMoreExpanded,
          text = "This test shows how SeymourText handles complex seeMoreContent with multiple UI elements and custom styling.",
          seeMoreMaxLines = 1,
          seeMoreContent = {
            Surface(
              color = Color(0xFFF3E5F5),
              modifier = Modifier.fillMaxWidth(),
            ) {
              TextButton(onClick = { setIsSeeMoreExpanded(!isSeeMoreExpanded) }) {
                Text(
                  text = if(isSeeMoreExpanded) "▲ Show Less" else "▼ Show More",
                  style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF9C27B0),
                  ),
                )
              }
            }
          },
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
          CompositionLocalProvider(
            LocalInspectionMode provides true,
          ) {
            content()
          }
        }
      }
    }
  }
}
