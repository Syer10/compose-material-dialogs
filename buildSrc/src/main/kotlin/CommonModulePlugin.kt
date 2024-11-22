import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

class CommonModulePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            applyPlugins()
        }
    }

    private fun Project.applyPlugins() {
        plugins.run {
            apply("com.android.library")
            apply("kotlin-multiplatform")
            apply("com.vanniktech.maven.publish")
            apply("shot")
            apply("org.jetbrains.dokka")
            apply("org.jetbrains.compose")
            apply("org.jetbrains.kotlin.plugin.compose")
        }
    }

    private fun DependencyHandler.implementation(dependency: String) {
        add("implementation", dependency)
    }

    private fun DependencyHandler.androidTestImplementation(dependency: String) {
        add("androidTestImplementation", dependency)
    }
}