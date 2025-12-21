package com.eygraber.seymour

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.AnnotatedString.Builder
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.TextUnit

/**
 * Displays text that can be expanded and collapsed, with inline "See More" and "See Less" links.
 *
 * The links are embedded directly within the text, providing a seamless user experience.
 *
 * @param onSeeMoreChange A callback that is invoked when the expansion state changes.
 * @param isSeeMoreExpanded Whether the text is expanded.
 * @param text The plain [String] to display.
 * @param seeMoreText The text to display for the "See More" link.
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
 * @param seeLessText The text to display for the "See Less" link. If `null`, the link will not be
 * shown.
 * @param seeMoreStyle The [SpanStyle] to apply to the "See More" link.
 * @param seeLessStyle The [SpanStyle] to apply to the "See Less" link.
 * @param seeLessMaxLines The maximum number of lines to display when expanded. Defaults to showing
 * the entire text. Must be greater than [seeMoreMaxLines].
 * @param onTextLayout A callback that is executed when the text layout is calculated.
 *
 * @throws IllegalArgumentException if [seeLessMaxLines] is not greater than [seeMoreMaxLines].
 */
@Composable
public fun SeymourText(
  onSeeMoreChange: (Boolean) -> Unit,
  isSeeMoreExpanded: Boolean,
  text: String,
  seeMoreText: String,
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
  seeLessText: String? = null,
  seeMoreStyle: SpanStyle = SpanStyle(),
  seeLessStyle: SpanStyle = seeMoreStyle,
  seeLessMaxLines: Int = Int.MAX_VALUE,
  onTextLayout: (TextLayoutResult) -> Unit = {},
) {
  SeymourText(
    onSeeMoreChange = onSeeMoreChange,
    isSeeMoreExpanded = isSeeMoreExpanded,
    text = AnnotatedString(text),
    seeMoreText = seeMoreText,
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
    seeLessText = seeLessText,
    seeMoreStyle = seeMoreStyle,
    seeLessStyle = seeLessStyle,
    seeLessMaxLines = seeLessMaxLines,
    onTextLayout = onTextLayout,
  )
}

/**
 * Displays an [AnnotatedString] that can be expanded and collapsed, with inline "See More" and
 * "See Less" links.
 *
 * The links are embedded directly within the text, providing a seamless user experience.
 *
 * @param onSeeMoreChange A callback that is invoked when the expansion state changes.
 * @param isSeeMoreExpanded Whether the text is expanded.
 * @param text The [AnnotatedString] to display.
 * @param seeMoreText The text to display for the "See More" link.
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
 * @param seeLessText The text to display for the "See Less" link. If `null`, the link will not be
 * shown.
 * @param seeMoreStyle The [SpanStyle] to apply to the "See More" link.
 * @param seeLessStyle The [SpanStyle] to apply to the "See Less" link.
 * @param seeLessMaxLines The maximum number of lines to display when expanded. Defaults to showing
 * the entire text. Must be greater than [seeMoreMaxLines].
 * @param onTextLayout A callback that is executed when the text layout is calculated.
 *
 * @throws IllegalArgumentException if [seeLessMaxLines] is not greater than [seeMoreMaxLines].
 */
@Composable
public fun SeymourText(
  onSeeMoreChange: (Boolean) -> Unit,
  isSeeMoreExpanded: Boolean,
  text: AnnotatedString,
  seeMoreText: String,
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
  seeLessText: String? = null,
  seeMoreStyle: SpanStyle = SpanStyle(),
  seeLessStyle: SpanStyle = seeMoreStyle,
  seeLessMaxLines: Int = Int.MAX_VALUE,
  onTextLayout: (TextLayoutResult) -> Unit = {},
) {
  require(seeLessMaxLines > seeMoreMaxLines) {
    "seeLessMaxLines must be greater than seeMoreMaxLines"
  }

  var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

  val mergedStyle = style.merge(
    color = color.takeOrElse { style.color.takeOrElse { LocalContentColor.current } },
    fontSize = fontSize,
    fontWeight = fontWeight,
    textAlign = textAlign ?: TextAlign.Unspecified,
    lineHeight = lineHeight,
    fontFamily = fontFamily,
    textDecoration = textDecoration,
    fontStyle = fontStyle,
    letterSpacing = letterSpacing,
  )

  val density = LocalDensity.current
  val containerSize = LocalWindowInfo.current.containerSize
  DisposableEffect(density, containerSize, mergedStyle, text) {
    textLayoutResult = null
    onDispose {}
  }

  val updatedOnSeeMoreChange by rememberUpdatedState(onSeeMoreChange)

  val seeMoreInteraction = remember {
    LinkInteractionListener {
      updatedOnSeeMoreChange(true)
      textLayoutResult = null
    }
  }

  val seeLessInteraction = remember {
    LinkInteractionListener {
      updatedOnSeeMoreChange(false)
      textLayoutResult = null
    }
  }

  val actualText = rememberMeasuredText(
    text = text,
    textLayoutResult = textLayoutResult,
    isSeeMoreExpanded = isSeeMoreExpanded,
    seeMoreText = seeMoreText,
    seeMoreStyle = seeMoreStyle,
    seeMoreInteraction = seeMoreInteraction,
    seeLessText = seeLessText,
    seeLessStyle = seeLessStyle,
    seeLessInteraction = seeLessInteraction,
    seeMoreMaxLines = seeMoreMaxLines,
    seeLessMaxLines = seeLessMaxLines,
    style = mergedStyle,
  )

  Text(
    text = actualText,
    style = mergedStyle,
    maxLines = if(isSeeMoreExpanded) seeLessMaxLines else seeMoreMaxLines,
    overflow = TextOverflow.Clip,
    onTextLayout = { result ->
      val shouldUpdate = textLayoutResult == null

      if(shouldUpdate) {
        textLayoutResult = result
      }

      onTextLayout(result)
    },
    modifier = modifier,
  )
}

@Composable
internal fun rememberMeasuredText(
  text: AnnotatedString,
  textLayoutResult: TextLayoutResult?,
  isSeeMoreExpanded: Boolean,
  seeMoreText: String,
  seeMoreStyle: SpanStyle,
  seeMoreInteraction: LinkInteractionListener,
  seeLessText: String?,
  seeLessStyle: SpanStyle,
  seeLessInteraction: LinkInteractionListener,
  seeMoreMaxLines: Int,
  seeLessMaxLines: Int,
  style: TextStyle,
): AnnotatedString {
  val textMeasurer = rememberTextMeasurer()

  val seeMoreAnnotated = rememberSeeMoreAnnotatedString(
    seeMoreText = seeMoreText,
    seeMoreStyle = seeMoreStyle,
    seeMoreInteraction = seeMoreInteraction,
  )

  val seeLessAnnotated = rememberSeeLessAnnotatedString(
    seeLessText = seeLessText,
    seeLessStyle = seeLessStyle,
    seeLessInteraction = seeLessInteraction,
  )

  return remember(
    text,
    textLayoutResult,
    isSeeMoreExpanded,
    seeMoreAnnotated,
    seeLessAnnotated,
    seeMoreMaxLines,
    seeLessMaxLines,
    style,
  ) {
    val isOverflow = textLayoutResult?.hasVisualOverflow == true
    val isTruncated = isOverflow && !isSeeMoreExpanded

    when {
      isTruncated -> createTruncatedText(
        text = text,
        seeMoreMaxLines = seeMoreMaxLines,
        seeMoreAnnotated = seeMoreAnnotated,
        style = style,
        textMeasurementProvider = ComposeTextMeasurementProvider(
          textMeasurer = textMeasurer,
          textLayoutResult = textLayoutResult,
        ),
      )

      isSeeMoreExpanded && seeLessAnnotated != null ->
        buildAnnotatedString(capacity = text.length + seeLessAnnotated.length) {
          append(text)
          append(seeLessAnnotated)
        }

      else -> text
    }
  }
}

@Composable
private fun rememberSeeMoreAnnotatedString(
  seeMoreText: String,
  seeMoreStyle: SpanStyle,
  seeMoreInteraction: LinkInteractionListener,
): AnnotatedString = remember(seeMoreText, seeMoreStyle) {
  buildAnnotatedString(capacity = seeMoreText.length) {
    withLink(
      link = LinkAnnotation.Clickable(
        tag = "expand",
        styles = TextLinkStyles(style = seeMoreStyle),
        linkInteractionListener = seeMoreInteraction,
      ),
    ) {
      append(seeMoreText)
    }
  }
}

@Composable
private fun rememberSeeLessAnnotatedString(
  seeLessText: String?,
  seeLessStyle: SpanStyle,
  seeLessInteraction: LinkInteractionListener,
): AnnotatedString? = remember(seeLessText, seeLessStyle, seeLessInteraction) {
  when(seeLessText) {
    null -> null
    else -> buildAnnotatedString(capacity = seeLessText.length) {
      withLink(
        link = LinkAnnotation.Clickable(
          tag = "collapse",
          styles = TextLinkStyles(seeLessStyle),
          linkInteractionListener = seeLessInteraction,
        ),
      ) {
        append(seeLessText)
      }
    }
  }
}

internal fun createTruncatedText(
  text: AnnotatedString,
  seeMoreMaxLines: Int,
  seeMoreAnnotated: AnnotatedString,
  style: TextStyle,
  textMeasurementProvider: TextMeasurementProvider,
): AnnotatedString {
  val lineIndex = seeMoreMaxLines - 1
  val lineStartIndex = textMeasurementProvider.getLineStart(lineIndex)
  val lineEndIndex = textMeasurementProvider.getLineEnd(lineIndex, visibleEnd = true)

  val seeMoreWidth = textMeasurementProvider.measureTextWidth(seeMoreAnnotated, style)
  val availableWidth = textMeasurementProvider.getLayoutWidth()
  val targetWidth = (availableWidth - seeMoreWidth).coerceAtLeast(0)

  val lineWithSeeMoreEndIndex = findTruncationIndex(
    text = text,
    lineStartIndex = lineStartIndex,
    lineEndIndex = lineEndIndex,
    style = style,
    targetWidth = targetWidth,
    textMeasurementProvider = textMeasurementProvider,
  )

  return buildAnnotatedString(
    capacity = text.length + seeMoreAnnotated.length + 1,
  ) {
    append(text.subSequence(startIndex = 0, endIndex = lineWithSeeMoreEndIndex))

    append(seeMoreAnnotated)

    // make sure to separate expandLinkAnnotated with a newline
    // so that hasVisualOverflow holds true, and expandLinkAnnotated
    // doesn't get connected to the overflowed text
    append("\n")
    append(text.subSequence(startIndex = lineWithSeeMoreEndIndex, endIndex = text.length))
  }
}

internal fun findTruncationIndex(
  text: AnnotatedString,
  lineStartIndex: Int,
  lineEndIndex: Int,
  style: TextStyle,
  targetWidth: Int,
  textMeasurementProvider: TextMeasurementProvider,
): Int {
  var lineWithExpandLinkEndIndex = lineEndIndex
  while(lineWithExpandLinkEndIndex > lineStartIndex) {
    val line = text.subSequence(startIndex = lineStartIndex, endIndex = lineWithExpandLinkEndIndex)
    val lineWidth = textMeasurementProvider.measureTextWidth(line, style)

    lineWithExpandLinkEndIndex--

    if(lineWidth < targetWidth) break
  }

  return lineWithExpandLinkEndIndex
}

private inline fun buildAnnotatedString(
  capacity: Int,
  builder: (Builder).() -> Unit,
): AnnotatedString =
  Builder(capacity = capacity).apply(builder).toAnnotatedString()

private class ComposeTextMeasurementProvider(
  private val textMeasurer: TextMeasurer,
  private val textLayoutResult: TextLayoutResult?,
) : TextMeasurementProvider {
  override fun measureTextWidth(text: AnnotatedString, style: TextStyle): Int {
    val result = textMeasurer.measure(
      text = text,
      style = style,
      overflow = TextOverflow.Clip,
      maxLines = 1,
    )

    return result.size.width
  }

  override fun getLineStart(lineIndex: Int): Int =
    textLayoutResult?.getLineStart(lineIndex) ?: 0

  override fun getLineEnd(lineIndex: Int, visibleEnd: Boolean): Int =
    textLayoutResult?.getLineEnd(
      lineIndex = lineIndex,
      visibleEnd = visibleEnd,
    ) ?: 0

  override fun getLayoutWidth(): Int = textLayoutResult?.size?.width ?: 0
}
