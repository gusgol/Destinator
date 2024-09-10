package me.goldhardt.destinator

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = DefaultConfig.compileSdk

        defaultConfig {
            minSdk = DefaultConfig.minSdk
        }

        compileOptions {
            sourceCompatibility = DefaultConfig.sourceCompatibility
            targetCompatibility = DefaultConfig.targetCompatibility
            isCoreLibraryDesugaringEnabled = true
        }
    }

    configureKotlin<KotlinAndroidProjectExtension>()

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android-desugar").get())
        add("implementation", libs.findLibrary("androidx-core-ktx").get())
    }
}

/**
 * Configure Kotlin options
 */
private inline fun <reified T : KotlinTopLevelExtension> Project.configureKotlin() = configure<T> {
    val warningsAsErrors: String? by project
    when (this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions
        else -> TODO("Unsupported project extension $this ${T::class}")
    }.apply {
        jvmTarget = DefaultConfig.jvmTarget
        allWarningsAsErrors = warningsAsErrors.toBoolean()
        freeCompilerArgs.add(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        )
    }
}