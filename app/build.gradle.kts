plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.image_compressor"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.image_compressor"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //for Zoomimage view

    implementation("com.jsibbold:zoomage:1.3.1")

    //jitpack zoom image

    implementation("com.github.MikeOrtiz:TouchImageView:3.6")

    //range slider
    implementation("com.google.android.material:material:1.4.0")

    //progress bar

    implementation("com.jpardogo.googleprogressbar:library:1.2.0")

    //circular imageview

    implementation("de.hdodenhof:circleimageview:3.1.0")

    //shaple image view

    implementation("com.google.android.material:material:1.2.0")

    implementation ("com.airbnb.android:lottie:3.4.0")

}