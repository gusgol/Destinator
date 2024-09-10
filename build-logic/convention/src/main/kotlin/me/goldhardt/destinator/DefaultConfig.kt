@file:Suppress("ConstPropertyName")

package me.goldhardt.destinator

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


object DefaultConfig {
    const val minSdk = 28
    const val compileSdk = 34
    const val targetSdk = 34
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    val sourceCompatibility = JavaVersion.VERSION_17
    val targetCompatibility = JavaVersion.VERSION_17
    val jvmTarget = JvmTarget.JVM_17
}