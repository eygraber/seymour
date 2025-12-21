@file:Suppress("NamedArguments", "UnnamedParameterUse")

package com.eygraber.seymour

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InlineSeymourTextMeasurementTest {
  @Test
  fun testFindTruncationIndexWithFakeProvider() {
    val text = AnnotatedString("Hello World Test Message")
    val style = TextStyle()
    val fakeProvider = FakeTextMeasurementProvider(pixelsPerChar = 10)

    val result = findTruncationIndex(
      text = text,
      lineStartIndex = 0,
      lineEndIndex = 15,
      style = style,
      targetWidth = 80, // Should fit ~8 characters at 10px each
      textMeasurementProvider = fakeProvider,
    )

    // Should truncate to fit within target width
    assertTrue(result >= 0)
    assertTrue(result <= 8) // 80px / 10px per char = 8 chars max
  }

  @Test
  fun testFindTruncationIndexEdgeCases() {
    val text = AnnotatedString("Test")
    val style = TextStyle()
    val fakeProvider = FakeTextMeasurementProvider(pixelsPerChar = 5)

    // Test with zero target width - should truncate to minimum
    val resultZero = findTruncationIndex(
      text = text,
      lineStartIndex = 0,
      lineEndIndex = 4,
      style = style,
      targetWidth = 0,
      textMeasurementProvider = fakeProvider,
    )
    assertEquals(0, resultZero)

    // Test with huge target width - should not truncate much
    val resultHuge = findTruncationIndex(
      text = text,
      lineStartIndex = 0,
      lineEndIndex = 4,
      style = style,
      targetWidth = 1000,
      textMeasurementProvider = fakeProvider,
    )
    assertTrue(resultHuge >= 3) // Should fit almost all text
  }

  @Test
  fun testFindTruncationIndexProgressiveWidths() {
    val text = AnnotatedString("Progressive width test")
    val style = TextStyle()
    val fakeProvider = FakeTextMeasurementProvider(pixelsPerChar = 6)

    // Test different target widths - should show progressive truncation
    val width30Result = findTruncationIndex(text, 0, text.length, style, 30, fakeProvider)
    val width60Result = findTruncationIndex(text, 0, text.length, style, 60, fakeProvider)
    val width120Result = findTruncationIndex(text, 0, text.length, style, 120, fakeProvider)

    // More width should allow more characters
    assertTrue(width30Result <= width60Result)
    assertTrue(width60Result <= width120Result)

    // Verify rough expectations based on 6px per char
    assertTrue(width30Result <= 5) // 30px / 6px = 5 chars
    assertTrue(width60Result <= 10) // 60px / 6px = 10 chars
    assertTrue(width120Result <= 20) // 120px / 6px = 20 chars
  }

  @Test
  fun testTextMeasurementProviderBehavior() {
    val fakeProvider = FakeTextMeasurementProvider(pixelsPerChar = 8)
    val text = AnnotatedString("Hello")
    val style = TextStyle()

    // Test width measurement
    assertEquals(40, fakeProvider.measureTextWidth(text, style)) // 5 chars * 8px
  }

  @Test
  fun testTruncationWithVaryingProviderSettings() {
    val text = AnnotatedString("Testing different provider settings")
    val style = TextStyle()

    // Test with different pixels per character settings
    val provider5px = FakeTextMeasurementProvider(pixelsPerChar = 5)
    val provider10px = FakeTextMeasurementProvider(pixelsPerChar = 10)

    val targetWidth = 100

    val result5px = findTruncationIndex(text, 0, text.length, style, targetWidth, provider5px)
    val result10px = findTruncationIndex(text, 0, text.length, style, targetWidth, provider10px)

    // 5px per char should allow more characters than 10px per char
    assertTrue(result5px >= result10px)

    // Verify approximate expectations
    assertTrue(result5px <= 20) // 100px / 5px = 20 chars max
    assertTrue(result10px <= 10) // 100px / 10px = 10 chars max
  }

  @Test
  fun testAnnotatedStringBehaviorForTruncation() {
    // Test AnnotatedString operations that the library uses
    val originalText = AnnotatedString("This is a test message for truncation logic")

    // Test subSequence operations
    val truncationPoint = 20
    val beforeTruncation = originalText.subSequence(0, truncationPoint)
    val afterTruncation = originalText.subSequence(truncationPoint, originalText.length)

    assertEquals("This is a test messa", beforeTruncation.text)
    assertEquals("ge for truncation logic", afterTruncation.text)

    // Test that reconstructing gives us the original
    assertEquals(originalText.text, beforeTruncation.text + afterTruncation.text)
  }

  @Test
  fun testWidthCalculationConcepts() {
    // Test the width calculation concepts
    val charWidth = 8
    val availableWidth = 120
    val linkWidth = 40

    // Calculate target width (available minus link)
    val targetWidth = (availableWidth - linkWidth).coerceAtLeast(0)
    assertEquals(80, targetWidth)

    // Calculate approximate characters that fit
    val maxChars = targetWidth / charWidth
    assertEquals(10, maxChars)

    // Test edge case where link is larger than available width
    val smallAvailable = 30
    val smallTarget = (smallAvailable - linkWidth).coerceAtLeast(0)
    assertEquals(0, smallTarget) // Should be at least 0
  }
}

private class FakeTextMeasurementProvider(
  private val pixelsPerChar: Int = 10,
  private val layoutWidth: Int = 200,
) : TextMeasurementProvider {
  override fun measureTextWidth(text: AnnotatedString, style: TextStyle): Int = text.length * pixelsPerChar

  override fun getLineStart(lineIndex: Int): Int =
    // Simple fake: assume each line starts at lineIndex * 20
    lineIndex * 20

  override fun getLineEnd(lineIndex: Int, visibleEnd: Boolean): Int =
    // Simple fake: assume each line ends at (lineIndex + 1) * 20
    (lineIndex + 1) * 20

  override fun getLayoutWidth(): Int = layoutWidth
}
