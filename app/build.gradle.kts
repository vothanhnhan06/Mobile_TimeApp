plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.timerapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.timerapp"
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
    implementation("com.android.volley:volley:1.2.1")
    implementation ("nl.dionsegijn:konfetti-xml:2.0.2")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.squareup.retrofit2:retrofit:2.1.0")
    implementation ("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.1.0")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation ("io.reactivex.rxjava3:rxjava:3.0.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")// Latest as of 2024
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation ("io.github.pilgr:paperdb:2.7.2")

}