
import me.goldhardt.destinator.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("dagger.hilt.android.plugin")
            }
            dependencies {
                add("ksp", libs.findLibrary("hilt.compiler").get())
                add("kspTest", libs.findLibrary("hilt.compiler").get())
                add("implementation", libs.findLibrary("hilt.android").get())

                /**
                 * Adds the Hilt Navigation Compose library to the project, e.g.: hiltViewModel()
                 * TODO Check if this is the best place to add this (instead of per-module)
                 */
                add("implementation", libs.findLibrary("androidx-hilt-navigation-compose").get())
            }
        }
    }
}