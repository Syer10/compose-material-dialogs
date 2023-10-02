plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.vanpra.composematerialdialogdemos"

    defaultConfig {
        applicationId = "com.vanpra.composematerialdialogs.app.android"

        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions.excludes.addAll(
        listOf(
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

    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.AndroidX.Compose.compilerVersion
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
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

    implementation(Dependencies.AndroidX.Compose.ui)
    implementation(Dependencies.AndroidX.Compose.material)
    implementation(Dependencies.AndroidX.Compose.materialIconsExtended)
    implementation(Dependencies.AndroidX.Compose.animation)
    implementation(Dependencies.AndroidX.Compose.foundation)

    implementation(Dependencies.AndroidX.Compose.activity)
    implementation(Dependencies.AndroidX.Compose.navigation)

    coreLibraryDesugaring(Dependencies.desugar)
}
