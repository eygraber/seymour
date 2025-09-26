package com.eygraber.seymour

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle

internal interface TextMeasurementProvider {
  /**
   * Measure the given text and return the width in pixels
   */
  fun measureTextWidth(text: AnnotatedString, style: TextStyle): Int

  /**
   * Get line start index for the given line in a layout
   */
  fun getLineStart(lineIndex: Int): Int

  /**
   * Get line end index for the given line in a layout
   */
  fun getLineEnd(lineIndex: Int, visibleEnd: Boolean = true): Int

  /**
   * Get the total width of the layout
   */
  fun getLayoutWidth(): Int
}
