plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

dependencies {
    implementation(compose.desktop.currentOs)

    implementation(projects.app.common)

    implementation(projects.composeMaterialDialogsCore)
    implementation(projects.composeMaterialDialogsColor)
    implementation(projects.composeMaterialDialogsDatetime)

    /*implementation(Dependencies.ComposeMaterialDialogs.core)
    implementation(Dependencies.ComposeMaterialDialogs.datetime)
    implementation(Dependencies.ComposeMaterialDialogs.color)*/

    implementation("cafe.adriel.voyager:voyager-navigator:1.0.0")

    implementation(Dependencies.DateTime.dateTime)
}

compose.desktop {
    application {
        mainClass = "com.vanpra.composematerialdialogdemos.desktop.MainKt"
    }
}