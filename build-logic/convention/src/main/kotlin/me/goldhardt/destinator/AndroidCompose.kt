package me.goldhardt.destinator

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose modules
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("implementation", libs.findLibrary("androidx-ui-tooling-preview").get())
            add("implementation", libs.findLibrary("androidx-ui-graphics").get())
            add("androidTestImplementation", platform(bom))
            add("debugImplementation", libs.findLibrary("androidx-ui-tooling").get())
            add("implementation", libs.findLibrary("androidx-material3").get())
        }

        testOptions {
            unitTests {
                // For Robolectric
                isIncludeAndroidResources = true
            }
        }
    }
}