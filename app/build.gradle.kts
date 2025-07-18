plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.penjualankopi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.penjualankopi"
        minSdk = 26
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
    // Gunakan deklarasi dari version catalog (libs)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // HAPUS BARIS-BARIS DI BAWAH INI KARENA SUDAH DIWAKILI OLEH LIBS DI ATAS
    // implementation 'androidx.appcompat:appcompat:1.6.1'
    // implementation 'com.google.android.material:material:1.12.0'
    // implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    // Dependensi untuk UI yang menarik (ini tidak duplikat, jadi biarkan saja)
}