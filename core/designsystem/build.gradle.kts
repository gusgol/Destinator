plugins {
    alias(libs.plugins.destinator.android.library)
    alias(libs.plugins.destinator.android.library.jacoco)
    alias(libs.plugins.destinator.android.library.compose)
    alias(libs.plugins.destinator.hilt)
}

android {
    namespace = "me.goldhardt.destinator.core.designsystem"

    defaultConfig {
        buildConfigField("String", "PLACES_API_KEY", "\"${project.findProperty("MAPS_API_KEY")}\"")
    }
}

dependencies {
    // UI
    implementation(libs.androidx.ui.text.google.fonts)

    // Coil
    implementation(libs.coil)
    implementation(libs.coil.compose)

    debugImplementation(libs.androidx.ui.tooling)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}