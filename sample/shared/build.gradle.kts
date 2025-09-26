plugins {
  id("com.eygraber.conventions-kotlin-multiplatform")
  id("com.eygraber.conventions-android-library")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-detekt")
}

android {
  namespace = "com.eygraber.seymour.sample.shared"
}

kotlin {
  kmpTargets(
    KmpTarget.Android,
    KmpTarget.Js,
    KmpTarget.Jvm,
    KmpTarget.WasmJs,
    project = project,
    ignoreDefaultTargets = true,
  )

  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.library)

        api(compose.foundation)
        api(compose.material3)
        api(compose.runtime)
      }
    }
  }
}
