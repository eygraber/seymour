plugins {
  kotlin("jvm")
  id("com.eygraber.conventions-compose-jetbrains")
  id("com.eygraber.conventions-kotlin-library")
  id("com.eygraber.conventions-detekt")
}

kotlin {
  dependencies {
    implementation(compose.desktop.currentOs)
    implementation(projects.sample.shared)
  }
}

compose.desktop {
  application {
    mainClass = "com.eygraber.seymour.sample.SeymourJvmKt"
  }
}
