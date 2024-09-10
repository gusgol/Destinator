plugins {
    alias(libs.plugins.destinator.android.library)
    alias(libs.plugins.destinator.android.library.compose)
    alias(libs.plugins.destinator.android.library.jacoco)
}

android {
    namespace = "me.goldhardt.destinator.core.common"
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}