plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    // dagger hilt
    id("com.google.dagger.hilt.android")
    // ksp
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.helloumi.ui"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {

    implementation(project(":domain"))

    //------------------------------------- Jetpack Compose -------------------------------------//
    ///////////////////////////////////////////// BOM /////////////////////////////////////////////
    val composeBom = platform(libs.compose.bom)

    implementation(platform(composeBom))
    androidTestImplementation(platform(composeBom))

    // Ui
    implementation(libs.compose.ui)
    implementation(libs.activity.compose)

    // Navigation
    implementation(libs.navigation.compose)

    // ViewModel
    implementation(libs.lifecycle.viewmodel.compose)

    // Material Design 3
    implementation(libs.material3)

    // Compose ViewBinding
    implementation(libs.ui.viewbinding)

    // Tooling
    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)

    // UI Tests
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)

    ///////////////////////////////////////////// BOM /////////////////////////////////////////////

    // Accompanist
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.placeholder)
    implementation(libs.accompanist.placeholder.material)

    // Hilt compose
    implementation(libs.hilt.navigation.compose)

    // ConstraintLayout for Compose
    implementation(libs.constraintlayout.compose)

    // Coil (Compose extension lib --> AsyncImage = load image from url)
    implementation(libs.coil.compose)

    //------------------------------------- Jetpack Compose -------------------------------------//

    // Android Studio Preview support
    implementation(libs.material)
    implementation(libs.annotation)

    // Places
    implementation(libs.places)

    // Dagger Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Testing
    implementation(libs.junit.ktx)
    androidTestImplementation(libs.junit.ktx)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.mockito.core)
    androidTestImplementation(libs.espresso.core)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)
}
