import org.gradle.api.Project
import org.gradle.api.provider.Provider

fun Project.git(vararg command: String): Provider<String> =
    providers
        .exec { commandLine("git", *command) }
        .standardOutput
        .asText
        .map { it.trim() }
