import java.io.FileInputStream
import java.util.Properties

plugins {
//    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.android.application")
    id("kotlin-kapt")
    id("realm-android")
    id("com.google.gms.google-services") //FCM
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

var properties = Properties()
properties.load(FileInputStream("local.properties"))
var NAVER_MAP = properties.getProperty("NAVER_MAP")

android {
    namespace = "com.example.front"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.front"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "NAVER_MAP", properties.getProperty("NAVER_MAP"))
        buildConfigField("String", "BASE_URL", properties.getProperty("BASE_URL"))
        buildConfigField("String", "MESSAGE_URL", properties.getProperty("MESSAGE_URL"))
        buildConfigField("String", "ADDRESS_KEY", properties.getProperty("ADDRESS_KEY"))
        buildConfigField("String", "FIREBASE_IMAGE_KEY", properties.getProperty("FIREBASE_IMAGE_KEY"))
        buildConfigField("String", "KAKAO_LOCATION_KEY", properties.getProperty("KAKAO_LOCATION_KEY"))
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["NAVER_MAP"] = NAVER_MAP
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-ktx:1.8.0'")
    implementation("androidx.fragment:fragment-ktx:1.6.0'")

    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
    implementation("com.google.firebase:firebase-messaging-ktx:23.2.1")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.gms:google-services:4.3.8")

    implementation("com.firebaseui:firebase-ui-storage:8.0.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    implementation("androidx.multidex:multidex:2.0.1")

    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.google.code.gson:gson:2.8.9")

    //OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    //image binding
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    //Prefrence DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    //Dagger
    implementation("com.google.dagger:dagger:2.0")
    // gps
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    implementation ("com.tickaroo.tikxml:annotation:0.8.13")
    implementation ("com.tickaroo.tikxml:core:0.8.13")
    implementation ("com.tickaroo.tikxml:retrofit-converter:0.8.13")
    kapt ("com.tickaroo.tikxml:processor:0.8.13")

    implementation("com.google.android.material:material:1.12.0")

    // Naver Maps SDK
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.naver.maps:map-sdk:3.19.1")

    implementation ("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable")

}