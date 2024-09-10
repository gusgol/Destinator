import com.android.build.gradle.LibraryExtension
import me.goldhardt.destinator.DefaultConfig
import me.goldhardt.destinator.configureFlavors
import me.goldhardt.destinator.configureKotlinAndroid
import me.goldhardt.destinator.configureProguard
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = DefaultConfig.targetSdk
                defaultConfig.testInstrumentationRunner = DefaultConfig.testInstrumentationRunner
                buildFeatures.buildConfig = true
                testOptions.animationsDisabled = true
                configureFlavors(this)
                configureProguard(this)
            }
            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))
            }
        }
    }
}