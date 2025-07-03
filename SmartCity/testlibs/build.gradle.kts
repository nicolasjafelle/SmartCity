import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "test.citron.testlibs"
    buildToolsVersion = libs.versions.buildTools.get()
    compileSdk = libs.versions.currentSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    testOptions {
        unitTests.all { test ->
            test.useJUnitPlatform() // <--- THIS IS CRUCIAL FOR JUNIT 5
        }
    }
}

dependencies {
    /******************************************************************
     ************************* IMPORTANT ******************************
     ******************************************************************
     ******** This module contains common test utilities to be ********
     ******** used across the different modules of the project ********
     ********** DO NOT include this module in production code.*********
     ***** use like this: testImplementation(project(":testlibs")) ****
     ***************** in your build.gradle.kts ***********************
     ******************************************************************
     ************************* IMPORTANT ******************************
     ******************************************************************/
    api(platform(libs.junit.bom)) // BOM para JUnit
    api(libs.junit.jupiter)
    api(libs.mockk)
    api(libs.kotest.assertions.core)
    api(libs.kotlinx.coroutines.test)
    api(libs.fixture)
    api(libs.turbine)
    runtimeOnly(libs.junit.platform.launcher)
}
