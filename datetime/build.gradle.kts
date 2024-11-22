import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("common-library")
}

kotlin {
    androidTarget {
        publishAllLibraryVariants()
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget = JvmTarget.JVM_1_8
                }
            }
        }
    }
    jvm {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget = JvmTarget.JVM_11
                }
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
                api(projects.composeMaterialDialogsCore)
                compileOnly(compose.ui)
                compileOnly(compose.foundation)
                compileOnly(compose.material)
                compileOnly(compose.animation)
                api(Dependencies.DateTime.dateTime)
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
    namespace = "com.vanpra.composematerialdialogs.datetime"
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    dependencies {
        add("coreLibraryDesugaring", Dependencies.desugar)
    }
}
