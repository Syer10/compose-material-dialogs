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

    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val uikitMain by creating {
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

                implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc07")
            }
        }
        val uikitTest by creating {
            dependsOn(commonTest)
        }

        listOf(
            "uikitX64",
            "uikitArm64",
            "uikitSimulatorArm64",
        ).forEach {
            getByName(it + "Main").dependsOn(uikitMain)
            getByName(it + "Test").dependsOn(uikitTest)
        }
    }
}

compose.experimental {
    uikit.application {
        bundleIdPrefix = "com.vanpra.composematerialdialogs.app.ios"
        projectName = "ComposeMaterialDialogs"
        // ./gradlew :app:ios:iosDeployIPhone13Debug
        deployConfigurations {
            simulator("IPhone13") {
                device = org.jetbrains.compose.experimental.dsl.IOSDevices.IPHONE_13
            }
        }
    }
}

kotlin {
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.all {
            // TODO: the current compose binary surprises LLVM, so disable checks for now.
            freeCompilerArgs = freeCompilerArgs + "-Xdisable-phases=VerifyBitcode"
        }
    }
}
