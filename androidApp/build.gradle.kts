plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.kuikly.kuiklymmkv"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.kuikly.kuiklymmkv"
        minSdk = 23
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        // AGP 8.x后，需要显示开启
        buildConfig = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.mmkv)
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.appcompat:appcompat:1.3.1")

    implementation("com.squareup.picasso:picasso:2.71828")

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.dynamicanimation:dynamicanimation:1.0.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
}