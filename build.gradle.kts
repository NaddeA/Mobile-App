// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {

    alias(libs.plugins.android.application) apply false// Android plugin
    alias(libs.plugins.kotlin.android) apply false// Kotlin Android plugin
    alias(libs.plugins.kotlin.compose) apply false// Jetpack Compose plugin
    alias(libs.plugins.hilt) apply false// Hilt plugin
    alias(libs.plugins.ksp) apply false

//    kotlin("kapt") version "2.0.21"
//    alias(libs.plugins.kotlin.kapt) // kapt for the dependencies
}

