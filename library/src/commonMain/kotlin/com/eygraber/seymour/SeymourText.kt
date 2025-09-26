package com.eygraber.seymour

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

/**
 * Displays text that can be expanded and collapsed, with a separate composable for the see
 * more/less control.
 *
 * Unlike the inline variant, this version displays the see more/less controls as a separate UI
 * element below the text.
 *
 * @param isSeeMoreExpanded Whether the text is expanded.
 * @param text The plain [String] to display.
 * @param seeMoreMaxLines The maximum number of lines to display when collapsed. Must be less
 * than [seeLessMaxLines].
 * @param modifier The [Modifier] to be applied to this composable.
 * @param color The color to apply to the text.
 * @param fontSize The size of the glyphs to use when painting the text.
 * @param fontStyle The typeface variant to use when drawing the letters (e.g., italic).
 * @param fontWeight The typeface thickness to use when painting the text (e.g., bold).
 * @param fontFamily The font family to use when rendering the text.
 * @param letterSpacing The amount of space to add between each letter in the text.
 * @param textDecoration The decorations to paint on the text (e.g., underline, strikethrough).
 * @param textAlign The alignment of the text within the lines of the paragraph.
 * @param lineHeight The line height for the paragraph in [TextUnit] unit, e.g., SP or EM.
 * @param style The base [TextStyle] to apply to the text.
 * @param seeLessMaxLines The maximum number of lines to display when expanded. Defaults to showing
 * the entire text. Must be greater than [seeMoreMaxLines].
 * @param overflow How visual overflow should be handled when the text is truncated.
 * @param onTextLayout A callback that is executed when the text layout is calculated.
 * @param seeMoreContent A composable that provides the UI for the "See More" and "See Less"
 * actions.
 *
 * @throws IllegalArgumentException if [seeLessMaxLines] is not greater than [seeMoreMaxLines].
 */
@Composable
public fun SeymourText(
  isSeeMoreExpanded: Boolean,
  text: String,
  seeMoreMaxLines: Int,
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified,
  fontSize: TextUnit = TextUnit.Unspecified,
  fontStyle: FontStyle? = null,
  fontWeight: FontWeight? = null,
  fontFamily: FontFamily? = null,
  letterSpacing: TextUnit = TextUnit.Unspecified,
  textDecoration: TextDecoration? = null,
  textAlign: TextAlign? = null,
  lineHeight: TextUnit = TextUnit.Unspecified,
  style: TextStyle = LocalTextStyle.current,
  seeLessMaxLines: Int = Int.MAX_VALUE,
  overflow: TextOverflow = TextOverflow.Ellipsis,
  onTextLayout: (TextLayoutResult) -> Unit = {},
  seeMoreContent: @Composable ColumnScope.() -> Unit,
) {
  SeymourText(
    isSeeMoreExpanded = isSeeMoreExpanded,
    text = AnnotatedString(text),
    seeMoreMaxLines = seeMoreMaxLines,
    modifier = modifier,
    color = color,
    fontSize = fontSize,
    fontStyle = fontStyle,
    fontWeight = fontWeight,
    fontFamily = fontFamily,
    letterSpacing = letterSpacing,
    textDecoration = textDecoration,
    textAlign = textAlign,
    lineHeight = lineHeight,
    style = style,
    seeLessMaxLines = seeLessMaxLines,
    overflow = overflow,
    onTextLayout = onTextLayout,
    seeMoreContent = seeMoreContent,
  )
}

/**
 * Displays an [AnnotatedString] that can be expanded and collapsed, with a separate composable for
 * the see more/less control.
 *
 * Unlike the inline variant, this version displays the see more/less controls as a separate UI
 * element below the text.
 *
 * @param isSeeMoreExpanded Whether the text is expanded.
 * @param text The [AnnotatedString] to display.
 * @param seeMoreMaxLines The maximum number of lines to display when collapsed. Must be less
 * than [seeLessMaxLines].
 * @param modifier The [Modifier] to be applied to this composable.
 * @param color The color to apply to the text.
 * @param fontSize The size of the glyphs to use when painting the text.
 * @param fontStyle The typeface variant to use when drawing the letters (e.g., italic).
 * @param fontWeight The typeface thickness to use when painting the text (e.g., bold).
 * @param fontFamily The font family to use when rendering the text.
 * @param letterSpacing The amount of space to add between each letter in the text.
 * @param textDecoration The decorations to paint on the text (e.g., underline, strikethrough).
 * @param textAlign The alignment of the text within the lines of the paragraph.
 * @param lineHeight The line height for the paragraph in [TextUnit] unit, e.g., SP or EM.
 * @param style The base [TextStyle] to apply to the text.
 * @param seeLessMaxLines The maximum number of lines to display when expanded. Defaults to showing
 * the entire text. Must be greater than [seeMoreMaxLines].
 * @param overflow How visual overflow should be handled when the text is truncated.
 * @param onTextLayout A callback that is executed when the text layout is calculated.
 * @param seeMoreContent A composable that provides the UI for the "See More" and "See Less"
 * actions.
 *
 * @throws IllegalArgumentException if [seeLessMaxLines] is not greater than [seeMoreMaxLines].
 */
@Composable
public fun SeymourText(
  isSeeMoreExpanded: Boolean,
  text: AnnotatedString,
  seeMoreMaxLines: Int,
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified,
  fontSize: TextUnit = TextUnit.Unspecified,
  fontStyle: FontStyle? = null,
  fontWeight: FontWeight? = null,
  fontFamily: FontFamily? = null,
  letterSpacing: TextUnit = TextUnit.Unspecified,
  textDecoration: TextDecoration? = null,
  textAlign: TextAlign? = null,
  lineHeight: TextUnit = TextUnit.Unspecified,
  style: TextStyle = LocalTextStyle.current,
  seeLessMaxLines: Int = Int.MAX_VALUE,
  overflow: TextOverflow = TextOverflow.Ellipsis,
  onTextLayout: (TextLayoutResult) -> Unit = {},
  seeMoreContent: @Composable ColumnScope.() -> Unit,
) {
  require(seeLessMaxLines > seeMoreMaxLines) {
    "seeLessMaxLines must be greater than seeMoreMaxLines"
  }

  Column(
    modifier = when {
      LocalInspectionMode.current -> modifier
      else -> modifier.animateContentSize()
    },
  ) {
    var isTruncated by remember { mutableStateOf(false) }

    Text(
      text = text,
      color = color,
      fontSize = fontSize,
      fontStyle = fontStyle,
      fontWeight = fontWeight,
      fontFamily = fontFamily,
      letterSpacing = letterSpacing,
      textDecoration = textDecoration,
      textAlign = textAlign,
      lineHeight = lineHeight,
      style = style,
      maxLines = if(isSeeMoreExpanded) seeLessMaxLines else seeMoreMaxLines,
      overflow = overflow,
      onTextLayout = { result ->
        isTruncated = result.hasVisualOverflow
        onTextLayout(result)
      },
    )

    if(isTruncated && !isSeeMoreExpanded || isSeeMoreExpanded) {
      seeMoreContent()
    }
  }
}
