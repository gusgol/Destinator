import com.android.build.gradle.LibraryExtension
import me.goldhardt.destinator.configureProguard
import me.goldhardt.destinator.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("destinator.android.library")
                apply("destinator.hilt")
            }
            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
                configureProguard(this)
            }

            dependencies {
                add("implementation", project(":core:designsystem"))
                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                add("androidTestImplementation", libs.findLibrary("androidx.lifecycle.runtime.testing").get())
            }
        }
    }
}
