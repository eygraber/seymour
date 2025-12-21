import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-detekt2")
  id("com.eygraber.conventions-compose-jetbrains")
}

kotlin {
  kmpTargets(
    KmpTarget.Js,
    KmpTarget.WasmJs,
    project = project,
    binaryType = BinaryType.Executable,
    webOptions = KmpTarget.WebOptions(
      isNodeEnabled = false,
      isBrowserEnabled = true,
      moduleName = "seymour-wasm",
    ),
    ignoreDefaultTargets = true,
  )
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser {
      commonWebpackConfig {
        outputFileName = "seymour-wasm.js"
      }
    }
  }

  sourceSets {
    webMain {
      dependencies {
        implementation(projects.sample.shared)

        implementation(libs.compose.foundation)
        implementation(libs.compose.material3)
      }
    }
  }
}
