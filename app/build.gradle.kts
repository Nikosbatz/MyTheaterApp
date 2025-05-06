plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.mytheaterapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mytheaterapp"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
    implementation("com.vinaygaba:creditcardview:1.0.4")
    implementation("com.vipulasri:ticketview:1.1.2")
    implementation ("com.google.zxing:core:3.5.2")
    implementation ("com.google.zxing:javase:3.5.2")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
}