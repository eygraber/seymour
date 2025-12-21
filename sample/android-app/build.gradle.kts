plugins {
  kotlin("android")
  id("com.android.application")
  id("com.eygraber.conventions-kotlin-library")
  id("com.eygraber.conventions-detekt2")
  id("com.eygraber.conventions-compose-jetbrains")
}

android {
  namespace = "com.eygraber.seymour.sample"

  compileSdk = libs.versions.android.sdk.compile.get().toInt()

  defaultConfig {
    applicationId = "com.eygraber.seymour.sample"
    targetSdk = libs.versions.android.sdk.target.get().toInt()
    minSdk = libs.versions.android.sdk.min.get().toInt()

    versionCode = 1
    versionName = "1.0.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    multiDexEnabled = true
  }

  buildTypes {
    named("release") {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles.clear()
      proguardFiles += project.file("proguard-rules.pro")
    }

    named("debug") {
      applicationIdSuffix = ".debug"

      versionNameSuffix = "-DEBUG"

      isMinifyEnabled = false
    }
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true
  }

  packaging {
    resources.pickFirsts += "META-INF/*"
  }

  dependencies {
    implementation(projects.sample.shared)

    coreLibraryDesugaring(libs.android.desugar)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.appCompat)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.uiTooling)
    implementation(libs.androidx.compose.uiToolingPreview)
  }
}
