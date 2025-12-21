plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-kmp-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt2")
}

kotlin {
  kmpTargets(
    KmpTarget.Android,
    KmpTarget.Js,
    KmpTarget.Jvm,
    KmpTarget.WasmJs,
    project = project,
    ignoreDefaultTargets = true,
    androidNamespace = "com.eygraber.seymour.sample.shared",
  )

  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.library)

        api(libs.compose.foundation)
        api(libs.compose.material3)
        api(libs.compose.runtime)
      }
    }
  }
}
