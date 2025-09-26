import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.eygraber.seymour.sample.Sample

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
  ComposeViewport {
    Sample()
  }
}
