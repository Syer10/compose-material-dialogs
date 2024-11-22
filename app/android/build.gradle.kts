import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.vanpra.composematerialdialogdemos"

    defaultConfig {
        applicationId = "com.vanpra.composematerialdialogs.app.android"

        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions.setExcludes(
        setOf(
            "META-INF/LICENSE",
            "META-INF/AL2.0",
            "META-INF/**",
        )
    )

    buildFeatures {
        viewBinding = true
        compose = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(projects.app.common)

    implementation(projects.composeMaterialDialogsCore)
    implementation(projects.composeMaterialDialogsColor)
    implementation(projects.composeMaterialDialogsDatetime)

//    implementation(Dependencies.ComposeMaterialDialogs.core)
//    implementation(Dependencies.ComposeMaterialDialogs.datetime)
//    implementation(Dependencies.ComposeMaterialDialogs.color)

    implementation(Dependencies.Google.material)
    implementation(Dependencies.AndroidX.coreKtx)

    implementation(Dependencies.DateTime.dateTime)

    implementation(compose.ui)
    implementation(compose.material)
    implementation(compose.materialIconsExtended)
    implementation(compose.animation)
    implementation(compose.foundation)

    implementation(Dependencies.AndroidX.Compose.activity)
    implementation(Dependencies.AndroidX.Compose.navigation)

    coreLibraryDesugaring(Dependencies.desugar)
}

tasks.withType<KotlinJvmCompile> {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_1_8
    }
}