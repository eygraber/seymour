package com.eygraber.seymour.sample

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eygraber.seymour.SeymourText

@Suppress("ModifierMissing")
@Composable
fun Sample() {
  var sampleText by remember {
    mutableStateOf(
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.",
    )
  }
  var seeMoreMaxLines by remember { mutableFloatStateOf(3F) }
  var seeLessMaxLines by remember { mutableStateOf(Float.MAX_VALUE) }
  var seeMoreText by remember { mutableStateOf("…see more") }
  var seeLessText by remember { mutableStateOf(" See less") }
  var isUseCustomSeeMoreStyle by remember { mutableStateOf(true) }
  var isExpanded1 by remember { mutableStateOf(false) }
  var isExpanded2 by remember { mutableStateOf(false) }
  var isExpanded3 by remember { mutableStateOf(false) }
  var isExpanded4 by remember { mutableStateOf(false) }
  var isExpanded5 by remember { mutableStateOf(false) }
  var isExpanded6 by remember { mutableStateOf(false) }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Header()

      ConfigurationCard(
        onSampleTextUpdate = { sampleText = it },
        onSeeMoreMaxLinesUpdate = { seeMoreMaxLines = it },
        onSeeLessMaxLinesUpdate = { seeLessMaxLines = it },
        onSeeMoreTextUpdate = { seeMoreText = it },
        onSeeLessTextUpdate = { seeLessText = it },
        onIsUseCustomSeeMoreStyleUpdate = { isUseCustomSeeMoreStyle = it },
        sampleText = sampleText,
        seeMoreMaxLines = seeMoreMaxLines,
        seeLessMaxLines = seeLessMaxLines,
        seeMoreText = seeMoreText,
        seeLessText = seeLessText,
        isUseCustomSeeMoreStyle = isUseCustomSeeMoreStyle,
      )

      HorizontalDivider()

      // Sample 1: Basic Usage
      SampleCard(
        title = "Inline Variant - Basic Usage",
        description = "Simple expandable text with default styling",
      ) {
        SeymourText(
          onSeeMoreChange = { isExpanded1 = it },
          isSeeMoreExpanded = isExpanded1,
          text = sampleText,
          seeMoreText = seeMoreText,
          seeLessText = seeLessText,
          seeMoreMaxLines = seeMoreMaxLines.toInt(),
          seeLessMaxLines = if(seeLessMaxLines >= 20F) Int.MAX_VALUE else seeLessMaxLines.toInt(),
        )
      }

      // Sample 2: Custom Styling
      SampleCard(
        title = "Inline Variant - Custom Styling",
        description = "Text with custom see more/less link styling",
      ) {
        SeymourText(
          onSeeMoreChange = { isExpanded2 = it },
          isSeeMoreExpanded = isExpanded2,
          text = sampleText,
          seeMoreText = seeMoreText,
          seeLessText = seeLessText,
          seeMoreMaxLines = seeMoreMaxLines.toInt(),
          seeLessMaxLines = if(seeLessMaxLines >= 20F) Int.MAX_VALUE else seeLessMaxLines.toInt(),
          seeMoreStyle = when {
            isUseCustomSeeMoreStyle -> SpanStyle(
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
            )

            else -> SpanStyle()
          },
          seeLessStyle = when {
            isUseCustomSeeMoreStyle -> SpanStyle(
              color = MaterialTheme.colorScheme.secondary,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
            )

            else -> SpanStyle()
          },
        )
      }

      // Sample 3: Different Text Style
      SampleCard(
        title = "Inline Variant - Custom Text Style",
        description = "Text with larger font size and different styling",
      ) {
        SeymourText(
          onSeeMoreChange = { isExpanded3 = it },
          isSeeMoreExpanded = isExpanded3,
          text = sampleText,
          seeMoreText = seeMoreText,
          seeLessText = seeLessText,
          seeMoreMaxLines = seeMoreMaxLines.toInt(),
          seeLessMaxLines = if(seeLessMaxLines >= 20F) Int.MAX_VALUE else seeLessMaxLines.toInt(),
          style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            lineHeight = 24.sp,
          ),
          seeMoreStyle = SpanStyle(
            color = Color.Blue,
            fontWeight = FontWeight.Medium,
          ),
          seeLessStyle = SpanStyle(
            color = Color.Red,
            fontWeight = FontWeight.Medium,
          ),
        )
      }

      // Sample 4: Different Text Style Expandable Only
      SampleCard(
        title = "Inline Variant - Custom Text Style Expandable Only",
        description = "Text with larger font size and different styling that can't be collapsed",
      ) {
        SeymourText(
          onSeeMoreChange = { if(it) isExpanded4 = it },
          isSeeMoreExpanded = isExpanded4,
          text = sampleText,
          seeMoreText = seeMoreText,
          seeMoreMaxLines = seeMoreMaxLines.toInt(),
          seeLessMaxLines = if(seeLessMaxLines >= 20F) Int.MAX_VALUE else seeLessMaxLines.toInt(),
          style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            lineHeight = 24.sp,
          ),
          seeMoreStyle = SpanStyle(
            color = Color.Blue,
            fontWeight = FontWeight.Medium,
          ),
        )
      }

      // Sample 5: Non-Inline Variant
      SampleCard(
        title = "Non-Inline Variant",
        description = "Text with see more/less links outside of the text",
      ) {
        SeymourText(
          isSeeMoreExpanded = isExpanded5,
          text = sampleText,
          seeMoreMaxLines = seeMoreMaxLines.toInt(),
          seeLessMaxLines = if(seeLessMaxLines >= 20F) Int.MAX_VALUE else seeLessMaxLines.toInt(),
          seeMoreContent = {
            TextButton(
              onClick = { isExpanded5 = !isExpanded5 },
            ) {
              Text(
                text = if(isExpanded5) seeLessText else seeMoreText,
                style = MaterialTheme.typography.bodyMedium.copy(
                  color = MaterialTheme.colorScheme.primary,
                ),
              )
            }
          },
        )
      }

      // Sample 6: Non-Inline Expandable Only Variant
      SampleCard(
        title = "Non-Inline Expandable Only Variant",
        description = "Text with a see more link outside of the text that can't be collapsed",
      ) {
        SeymourText(
          isSeeMoreExpanded = isExpanded6,
          text = sampleText,
          seeMoreMaxLines = seeMoreMaxLines.toInt(),
          seeLessMaxLines = if(seeLessMaxLines >= 20F) Int.MAX_VALUE else seeLessMaxLines.toInt(),
          seeMoreContent = {
            if(!isExpanded6) {
              TextButton(
                onClick = { isExpanded6 = !isExpanded6 },
              ) {
                Text(
                  text = seeMoreText,
                  style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                  ),
                )
              }
            }
          },
        )
      }
    }
  }
}

@Composable
private fun ColumnScope.Header() {
  Text(
    text = "SeymourText Demo",
    style = MaterialTheme.typography.headlineMedium,
    fontWeight = FontWeight.Bold,
  )

  Text(
    text = """
        |Showcasing both inline and non-inline variants.
        |Try resizing the window to see how the 'See more' link position adapts automatically!
    """.trimMargin(),
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
  )
}

@Composable
private fun ConfigurationCard(
  onSampleTextUpdate: (String) -> Unit,
  onSeeMoreMaxLinesUpdate: (Float) -> Unit,
  onSeeLessMaxLinesUpdate: (Float) -> Unit,
  onSeeMoreTextUpdate: (String) -> Unit,
  onSeeLessTextUpdate: (String) -> Unit,
  onIsUseCustomSeeMoreStyleUpdate: (Boolean) -> Unit,
  sampleText: String,
  seeMoreMaxLines: Float,
  seeLessMaxLines: Float,
  seeMoreText: String,
  seeLessText: String,
  isUseCustomSeeMoreStyle: Boolean,
) {
  ElevatedCard(
    modifier = Modifier.fillMaxWidth(),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Text(
        text = "Configuration",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
      )

      // Text Content Input
      OutlinedTextField(
        value = sampleText,
        onValueChange = onSampleTextUpdate,
        label = { Text("Sample Text") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 2,
        maxLines = 4,
      )

      // Collapsed Max Lines Slider
      Column {
        Text(text = "Collapsed Max Lines: $seeMoreMaxLines")
        Slider(
          value = seeMoreMaxLines,
          onValueChange = onSeeMoreMaxLinesUpdate,
          valueRange = 1F..10F,
          steps = 8,
        )
      }

      // Expanded Max Lines Slider
      Column {
        val maxLines = if(seeLessMaxLines >= 20F) "Unlimited" else seeLessMaxLines
        Text(text = "Expanded Max Lines: $maxLines")

        Slider(
          value = seeLessMaxLines,
          onValueChange = onSeeLessMaxLinesUpdate,
          valueRange = 1F..20F,
          steps = 18,
        )
      }

      // See More/Less Text Configuration
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        OutlinedTextField(
          value = seeMoreText,
          onValueChange = onSeeMoreTextUpdate,
          label = { Text("See More Text") },
          modifier = Modifier.weight(1F),
        )
        OutlinedTextField(
          value = seeLessText,
          onValueChange = onSeeLessTextUpdate,
          label = { Text("See Less Text") },
          modifier = Modifier.weight(1F),
        )
      }

      // Custom Styling Toggle
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Text("Custom See More/Less Styling")
        Spacer(modifier = Modifier.weight(1F))
        Switch(
          checked = isUseCustomSeeMoreStyle,
          onCheckedChange = onIsUseCustomSeeMoreStyleUpdate,
        )
      }
    }
  }
}

@Composable
private fun SampleCard(
  title: String,
  description: String,
  content: @Composable () -> Unit,
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .animateContentSize(),
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
      )

      Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      Spacer(modifier = Modifier.height(4.dp))

      content()
    }
  }
}
