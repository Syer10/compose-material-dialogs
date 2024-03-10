import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    val configuration: KotlinNativeTarget.() -> Unit = {
        binaries {
            executable {
                entryPoint = "com.vanpra.composematerialdialogs.ios.main"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal",
                    "-linker-option", "-framework", "-linker-option", "CoreText",
                    "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                )
                // TODO: the current compose binary surprises LLVM, so disable checks for now.
                freeCompilerArgs = freeCompilerArgs + "-Xdisable-phases=VerifyBitcode"
            }
        }
    }
    iosX64("uikitX64", configuration)
    iosArm64("uikitArm64", configuration)
    iosSimulatorArm64("uikitSimulatorArm64", configuration)

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyHierarchyTemplate {
        common {
            group("uikit") {
                withIosArm64()
                withIosX64()
                withIosSimulatorArm64()
            }
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting
        getByName("uikitMain") {
            dependsOn(commonMain)
            dependencies {
                implementation(projects.app.common)

                implementation(projects.composeMaterialDialogsCore)
                implementation(projects.composeMaterialDialogsColor)
                implementation(projects.composeMaterialDialogsDatetime)

                /*implementation(Dependencies.ComposeMaterialDialogs.core)
                implementation(Dependencies.ComposeMaterialDialogs.datetime)
                implementation(Dependencies.ComposeMaterialDialogs.color)*/

                implementation(Dependencies.DateTime.dateTime)

                implementation("io.github.aakira:napier:2.6.1")

                implementation("cafe.adriel.voyager:voyager-navigator:1.0.0")
            }
        }
    }
}

//compose.experimental {
//    uikit.application {
//        bundleIdPrefix = "com.vanpra.composematerialdialogs.app.ios"
//        projectName = "ComposeMaterialDialogs"
//        // ./gradlew :app:ios:iosDeployIPhone13Debug
//        deployConfigurations {
//            simulator("IPhone13") {
//                device = org.jetbrains.compose.experimental.dsl.IOSDevices.IPHONE_13
//            }
//            simulator("IPadPro11") {
//                device = org.jetbrains.compose.experimental.dsl.IOSDevices.IPAD_PRO_11_INCH
//            }
//        }
//    }
//}

kotlin {
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.all {
            // TODO: the current compose binary surprises LLVM, so disable checks for now.
            freeCompilerArgs = freeCompilerArgs + "-Xdisable-phases=VerifyBitcode"
        }
    }
}
