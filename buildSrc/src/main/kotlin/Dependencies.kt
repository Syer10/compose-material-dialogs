object Dependencies {
    const val desugar = "com.android.tools:desugar_jdk_libs:2.0.3"

    object ComposeMaterialDialogs {
        const val version = "0.8.0"

        const val core = "ca.gosyer:compose-material-dialogs-core:$version"
        const val datetime = "ca.gosyer:compose-material-dialogs-datetime:$version"
        const val color = "ca.gosyer:compose-material-dialogs-color:$version"
    }

    object Ktlint {
        const val version = "0.45.2"
    }

    object Kotlin {
        private const val version = "1.9.10"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object DateTime {
        private const val version = "0.4.1"
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:$version"
    }

    object Shot {
        private const val version = "5.14.1"
        const val core = "com.karumi:shot:$version"
        const val android = "com.karumi:shot-android:$version"
    }

    object Google {
        const val material = "com.google.android.material:material:1.6.0"
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.7.0"

        object Testing {
            const val version = "1.4.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"
            const val runner = "androidx.test:runner:$version"
        }

        object Compose {
            const val compilerVersion = "1.5.3"
            const val mppVersion = "1.5.2"

            const val runtime = "androidx.compose.ui:ui:1.5.0"
            const val ui = "androidx.compose.ui:ui:1.5.0"
            const val material = "androidx.compose.material:material:1.5.0"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended:1.5.0"
            const val animation = "androidx.compose.animation:animation:1.5.0"
            const val foundation = "androidx.compose.foundation:foundation:1.5.0"

            const val testing = "androidx.compose.ui:ui-test-junit4:1.5.0"
            const val activity = "androidx.activity:activity-compose:1.4.0"
            const val navigation = "androidx.navigation:navigation-compose:2.4.2"
        }
    }
}