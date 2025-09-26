import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
  id("com.eygraber.conventions-publish-maven-central")
  alias(libs.plugins.paparazzi)
}

kotlin {
  explicitApi()

  defaultKmpTargets(
    project = project,
  )

  js {
    compilerOptions.optIn.add("kotlin.js.ExperimentalWasmJsInterop")

    browser {
      testTask {
        enabled = false
      }
    }
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    compilerOptions.optIn.add("kotlin.js.ExperimentalWasmJsInterop")
  }

  sourceSets {
    androidUnitTest.dependencies {
      implementation(libs.test.compose.uiManifest)
      implementation(libs.test.junit)
      implementation(libs.test.paparazzi)

      implementation(libs.test.androidx.junit)
      implementation(libs.test.compose.uiJunit4)
      implementation(libs.test.robolectric)
    }

    commonMain.dependencies {
      implementation(compose.foundation)
      implementation(libs.compose.material3)
      implementation(compose.runtime)
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
      @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
      implementation(compose.uiTest)
    }

    jvmTest.dependencies {
      implementation(compose.desktop.currentOs)
    }
  }
}

android {
  namespace = "com.eygraber.seymour"

  testOptions.unitTests.isIncludeAndroidResources = true

  dependencies {
    debugImplementation(compose.uiTooling)
  }
}
