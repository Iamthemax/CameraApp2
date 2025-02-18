plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.cameraapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cameraapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Retrofit library for network requests
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Retrofit converter for Gson (if you are using JSON responses)
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp for making network requests
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")

    // OkHttp logging interceptor (optional, for logging network requests)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Gson (required by Retrofit)
    implementation ("com.google.code.gson:gson:2.8.8")

}