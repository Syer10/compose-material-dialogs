import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.vanpra.composematerialdialogs.test.utils"

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packagingOptions.excludes.addAll(
        listOf(
            "META-INF/LICENSE",
            "META-INF/AL2.0",
            "META-INF/**",
            "META-INF/*.kotlin_module",
            "META-INF/*.kotlin_module"
        )
    )

    buildFeatures {
        buildConfig = false
        compose = true
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    api(projects.composeMaterialDialogsCore)

    implementation(compose.ui)
    implementation(compose.material)
    implementation(compose.materialIconsExtended)
    implementation(Dependencies.AndroidX.Compose.activity)

    @OptIn(ExperimentalComposeLibrary::class)
    implementation(compose.uiTest)
    implementation(Dependencies.Shot.android)
}
