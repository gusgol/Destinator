package me.goldhardt.destinator

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

/**
 * Configure Proguard for the module.
 * This assumes Proguard files will be present in the module.
 */
internal fun Project.configureProguard(
    libraryExtension: LibraryExtension,
) {
    libraryExtension.apply {
        defaultConfig {
            consumerProguardFiles("consumer-rules.pro")
        }
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }
}