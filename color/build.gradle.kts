plugins {
    id("common-library")
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

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.composeMaterialDialogsCore)
                compileOnly(compose.ui)
                compileOnly(compose.foundation)
                compileOnly(compose.material)
                compileOnly(compose.animation)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(jvmCommon)
            dependencies {
            }
        }
        val jvmTest by getting

        val androidMain by getting {
            dependsOn(jvmCommon)
            dependencies {
                compileOnly(Dependencies.AndroidX.Compose.ui)
                compileOnly(Dependencies.AndroidX.Compose.foundation)
                compileOnly(Dependencies.AndroidX.Compose.material)
                compileOnly(Dependencies.AndroidX.Compose.animation)
            }
        }
        val androidUnitTest by getting

        val iosMain by creating {
            dependsOn(commonMain)
        }
        val iosTest by creating {
            dependsOn(commonTest)
        }

        listOf(
            "iosX64",
            "iosArm64",
            "iosSimulatorArm64",
        ).forEach {
            getByName(it + "Main").dependsOn(iosMain)
            getByName(it + "Test").dependsOn(iosTest)
        }
    }
}

shot {
    tolerance = 1.0 // Tolerance needed for CI
}

android {
    namespace = "com.vanpra.composematerialdialogs.color"
}