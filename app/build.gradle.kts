plugins {
    alias(libs.plugins.destinator.android.application)
    alias(libs.plugins.destinator.android.application.compose)
    alias(libs.plugins.destinator.android.application.jacoco)
    alias(libs.plugins.destinator.android.application.flavors)
    alias(libs.plugins.destinator.hilt)
}

android {
    namespace = "me.goldhardt.destinator"

    defaultConfig {
        applicationId = "me.goldhardt.destinator"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }


    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Modules
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.feature.trips)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

//    // UI
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//    implementation(libs.androidx.navigation.compose)
//
//    // Hilt
//    implementation(libs.hilt.android)
//    ksp(libs.hilt.compiler)
//    kspTest(libs.hilt.compiler)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
}