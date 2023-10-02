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

    sourceSets {
        val commonMain by getting {
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
                api(kotlin("stdlib-jdk8"))
            }
        }
        val jvmTest by getting

        val androidMain by getting {
            dependsOn(jvmCommon)
            dependencies {
                api(kotlin("stdlib-jdk8"))
                api(Dependencies.AndroidX.Compose.ui)
                api(Dependencies.AndroidX.Compose.foundation)
                api(Dependencies.AndroidX.Compose.material)
                api(Dependencies.AndroidX.Compose.animation)
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

android {
    namespace = "com.vanpra.composematerialdialogs.app.common"
}