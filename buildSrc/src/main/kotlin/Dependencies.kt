object Dependencies {
    const val desugar = "com.android.tools:desugar_jdk_libs:2.1.4"

    object ComposeMaterialDialogs {
        const val version = "0.9.6"

        const val core = "ca.gosyer:compose-material-dialogs-core:$version"
        const val datetime = "ca.gosyer:compose-material-dialogs-datetime:$version"
        const val color = "ca.gosyer:compose-material-dialogs-color:$version"
    }

    object Ktlint {
        const val version = "0.45.2"
    }

    object Kotlin {
        private const val version = "2.2.0"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object DateTime {
        private const val version = "0.6.1"
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:$version"
    }

    object Shot {
        private const val version = "6.1.0"
        const val core = "com.karumi:shot:$version"
        const val android = "com.karumi:shot-android:$version"
    }

    object Google {
        const val material = "com.google.android.material:material:1.12.0"
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.15.0"

        object Testing {
            const val version = "1.6.2"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"
            const val runner = "androidx.test:runner:$version"
        }

        object Compose {
            const val activity = "androidx.activity:activity-compose:1.10.0"
            const val navigation = "androidx.navigation:navigation-compose:2.8.5"
        }
    }
}