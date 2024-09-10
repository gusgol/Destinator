plugins {
    alias(libs.plugins.destinator.android.library)
    alias(libs.plugins.destinator.android.library.jacoco)
    alias(libs.plugins.destinator.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "me.goldhardt.destinator.data"
}

dependencies {
    // Modules
    implementation(projects.core.ai)
    implementation(projects.core.database)
    implementation(projects.core.places)

    implementation(libs.kotlinx.serialization)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}