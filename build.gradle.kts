import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.compose") version "1.2.1" apply false
    //id("com.diffplug.spotless") version "6.0.4"
    id("org.jetbrains.dokka") version "1.7.10"
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(Dependencies.Kotlin.gradlePlugin)
        classpath("com.android.tools.build:gradle:7.3.0-rc01")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.18.0")
        classpath(Dependencies.Shot.core)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(projectDir.resolve("docs/api"))
}

subprojects {
    //plugins.apply("com.diffplug.spotless")
    /*spotless {
        kotlin {
            target("*")
            ktlint(Dependencies.Ktlint.version)
        }
    }*/

    tasks.withType<Test> {
        testLogging {
            showStandardStreams = true
        }
    }

    plugins.withType<com.android.build.gradle.BasePlugin> {
        configure<com.android.build.gradle.BaseExtension> {
            compileSdkVersion(32)
            defaultConfig {
                minSdk = 21
                targetSdk = 32

                testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
                testApplicationId = "com.vanpra.composematerialdialogs.test"
            }
            compileOptions {
                sourceCompatibility(JavaVersion.VERSION_1_8)
                targetCompatibility(JavaVersion.VERSION_1_8)
            }
            sourceSets {
                named("main") {
                    val altManifest = file("src/androidMain/AndroidManifest.xml")
                    if (altManifest.exists()) {
                        manifest.srcFile(altManifest.path)
                    }
                }
            }
        }
    }

    plugins.withType<com.karumi.shot.ShotPlugin> {
        configure<com.karumi.shot.ShotExtension> {
            tolerance = 1.0
        }
    }


    // Read in the signing.properties file if it is exists
    val signingPropsFile = rootProject.file("release/signing.properties")
    if (signingPropsFile.exists()) {
        java.util.Properties().apply {
            signingPropsFile.inputStream().use {
                load(it)
            }
        }.forEach { key1, value1 ->
            val key = key1.toString()
            val value = value1.toString()
            if (key == "signing.secretKeyRingFile") {
                // If this is the key ring, treat it as a relative path
                project.ext.set(key, rootProject.file(value).absolutePath)
            } else {
                project.ext.set(key, value)
            }
        }
    }
}
