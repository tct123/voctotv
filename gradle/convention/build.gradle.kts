plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
  google()
}

dependencies {
  compileOnly(libs.android.gradlePlugin)
}

gradlePlugin {
  plugins {
    register("versionConvention") {
      id = "justjanne.version"
      implementationClass = "VersionConvention"
    }
  }
}
