plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(compose.desktop.currentOs)

    implementation(projects.app.common)

    implementation(projects.core)
    implementation(projects.color)
    implementation(projects.datetime)

    /*implementation(Dependencies.ComposeMaterialDialogs.core)
    implementation(Dependencies.ComposeMaterialDialogs.datetime)
    implementation(Dependencies.ComposeMaterialDialogs.color)*/

    implementation("ca.gosyer:voyager-navigator:1.0.0-rc06")

    implementation(Dependencies.DateTime.dateTime)
}

compose.desktop {
    application {
        mainClass = "com.vanpra.composematerialdialogdemos.MainKt"
    }
}