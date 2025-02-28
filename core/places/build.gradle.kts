plugins {
    alias(libs.plugins.destinator.android.library)
    alias(libs.plugins.destinator.android.library.jacoco)
    alias(libs.plugins.destinator.android.library.compose)
    alias(libs.plugins.destinator.hilt)
}

android {
    namespace = "me.goldhardt.destinator.core.places"

    defaultConfig {
        buildConfigField("String", "PLACES_API_KEY", "\"${project.findProperty("MAPS_API_KEY")}\"")
    }
}

dependencies {
    // Google Places
    implementation(libs.google.places)
    implementation(projects.core.designsystem) // TODO the the place photo... Should not depend on other core libs, though.

    // UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}