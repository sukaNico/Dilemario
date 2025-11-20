plugins {
    // Si tu proyecto usa version catalog y aliases en root build.gradle.kts, mantén estos alias.
    // Si al hacer sync da error en alias(...) entonces cámbialos por: id("com.android.application") y id("org.jetbrains.kotlin.android")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.dilemario"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.dilemario"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(libs.androidx.foundation)
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation:1.6.0")
}
