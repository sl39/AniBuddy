// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.3.13" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()

    }
    dependencies {
        classpath("io.realm:realm-gradle-plugin:10.15.1")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://repository.map.naver.com/archive/maven")
    }
}