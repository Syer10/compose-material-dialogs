plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
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

    implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc07")

    implementation(Dependencies.DateTime.dateTime)
}

compose.desktop {
    application {
        mainClass = "com.vanpra.composematerialdialogdemos.desktop.MainKt"
    }
}