import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinAndroidKsp)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "test.citron.mapslib"
    buildToolsVersion = libs.versions.buildTools.get()
    compileSdk = libs.versions.currentSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        resValues = true
        compose = true
    }

    buildTypes {
        debug {
            resValue("string", "google_maps_api_key", "AIzaSyDz-OV_Q6liMrYCAtNbsgveeBbepiZ3P6U")
        }

        release {
            resValue("string", "google_maps_api_key", "AIzaSyDz-OV_Q6liMrYCAtNbsgveeBbepiZ3P6U")

            isMinifyEnabled = true
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

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.maps.compose)
//    implementation(libs.maps.utils)
}
