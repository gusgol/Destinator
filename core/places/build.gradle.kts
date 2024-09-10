plugins {
    alias(libs.plugins.destinator.android.library)
    alias(libs.plugins.destinator.android.library.jacoco)
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

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}