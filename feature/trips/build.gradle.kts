plugins {
    alias(libs.plugins.destinator.android.feature)
    alias(libs.plugins.destinator.android.library.compose)
    alias(libs.plugins.destinator.android.library.jacoco)
}

android {
    namespace = "me.goldhardt.destinator.feature.trips"

    defaultConfig {
        resValue("string", "maps_key", "\"${project.findProperty("MAPS_API_KEY")}\"")
    }
}

dependencies {
    // Modules
    implementation(projects.core.designsystem)
    implementation(projects.core.data)
    implementation(projects.core.common)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.viewModel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.accompanist)

    // UI
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    kspTest(libs.hilt.compiler)

    // Google Maps
    implementation(libs.google.maps)

    // Coil
    implementation(libs.coil)
    implementation(libs.coil.compose)

    debugImplementation(libs.androidx.ui.tooling)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}