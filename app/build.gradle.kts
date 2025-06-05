plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.zyra"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.zyra"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.room.common)
    testImplementation(libs.junit)
    implementation (libs.lottie)
    implementation (libs.play.services.base)
    implementation (libs.material.v1100)
    implementation(libs.glide);
    implementation(libs.material.v190)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.room.runtime)
    testImplementation(libs.ext.junit)
    testImplementation(libs.espresso.core)
    annotationProcessor (libs.room.compiler)
    implementation (libs.room.rxjava2)
    annotationProcessor (libs.compiler)
    implementation (libs.firebase.firestore)
    implementation (libs.firebase.auth.v2110)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.intents)


}