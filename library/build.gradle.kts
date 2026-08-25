import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-kmp-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt2")
  id("com.eygraber.conventions-publish-maven-central")
  // alias(libs.plugins.paparazzi)
}

kotlin {
  explicitApi()

  defaultKmpTargets(
    project = project,
    androidNamespace = "com.eygraber.seymour",
  )

  androidLibrary {
    withHostTest {
      isIncludeAndroidResources = true
    }
  }

  js {
    compilerOptions.optIn.add("kotlin.js.ExperimentalWasmJsInterop")

    // Compose UI tests need the Skiko runtime to be bundled by webpack, which only happens
    // if an executable binary is declared - https://youtrack.jetbrains.com/issue/CMP-4906
    binaries.executable()

    browser {
      testTask {
        enabled = false
      }
    }
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    compilerOptions.optIn.add("kotlin.js.ExperimentalWasmJsInterop")

    binaries.executable()
  }

  sourceSets {
    named("androidHostTest").dependencies {
      implementation(libs.test.compose.uiManifest)
      implementation(libs.test.junit)
      implementation(libs.test.paparazzi)

      implementation(libs.test.androidx.junit)
      implementation(libs.test.compose.uiJunit4)
      implementation(libs.test.robolectric)
    }

    commonMain.dependencies {
      implementation(libs.compose.foundation)
      implementation(libs.compose.material3)
      implementation(libs.compose.runtime)
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
      implementation(libs.compose.uiTest)
    }

    jvmTest.dependencies {
      implementation(compose.desktop.currentOs)
    }
  }
}
