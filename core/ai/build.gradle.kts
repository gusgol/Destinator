plugins {
    alias(libs.plugins.destinator.android.library)
    alias(libs.plugins.destinator.android.library.jacoco)
    alias(libs.plugins.destinator.hilt)
}

android {
    namespace = "me.goldhardt.destinator.core.ai"

    defaultConfig {
        buildConfigField("String", "GEMINI_API_KEY", "\"${project.findProperty("G_API_KEY")}\"")
    }
}

dependencies {
    implementation(libs.gemini)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}