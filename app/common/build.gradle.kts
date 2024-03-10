import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}


kotlin {
    androidTarget {
        publishAllLibraryVariants()
        compilations {
            all {
                kotlinOptions.jvmTarget = "1.8"
            }
        }
    }
    jvm {
        compilations {
            all {
                kotlinOptions.jvmTarget = "11"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            group("jvmCommon") {
                withAndroidTarget()
                withJvm()
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(kotlin("stdlib-common"))
                api(compose.ui)
                api(compose.foundation)
                api(compose.material)
                api(compose.animation)
                api(Dependencies.DateTime.dateTime)
                api(projects.composeMaterialDialogsCore)
                api(projects.composeMaterialDialogsColor)
                api(projects.composeMaterialDialogsDatetime)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}

android {
    namespace = "com.vanpra.composematerialdialogs.app.common"
}