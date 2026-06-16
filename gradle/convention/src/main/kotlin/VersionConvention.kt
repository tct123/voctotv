import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.kotlin.dsl.configure

class VersionConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            val commit = git("rev-parse", "HEAD").orNull
            val code = git("rev-list", "--count", "HEAD", "--tags").orNull
            val name = git("describe", "--always", "--tags", "HEAD").orNull

            extensions.configure<BasePluginExtension> {
                if (name == null) {
                    archivesName.set("${rootProject.name}-${project.name}")
                } else {
                    archivesName.set("${rootProject.name}-${project.name}-$name")
                }
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    versionCode = code?.toIntOrNull()
                    versionName = name
                }
            }
        }
    }
}
