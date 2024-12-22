import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    val configuration: KotlinNativeTarget.() -> Unit = {
        binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
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

                implementation("io.github.aakira:napier:2.7.1")

                implementation("cafe.adriel.voyager:voyager-navigator:1.0.1")
            }
        }
    }
}
