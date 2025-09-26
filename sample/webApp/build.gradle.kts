import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-detekt")
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
        implementation(compose.foundation)
        implementation(compose.material3)

        implementation(projects.sample.shared)
      }
    }
  }
}
