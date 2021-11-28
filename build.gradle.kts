import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0-rc3"
}

group = "com.frogobox"
version = "1.0"

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.github.amirisback.nutrition-framework:nutritioncore:0.0.2-beta01")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2-native-mt")
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "nutrition-framework-desktop"
            packageVersion = "1.0.0"
        }
    }
}