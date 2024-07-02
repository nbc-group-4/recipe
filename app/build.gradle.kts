import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id ("kotlin-parcelize")
}

val properties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "nbc.group.recipes"
    compileSdk = 34

    defaultConfig {
        applicationId = "nbc.group.recipes"
        minSdk = 28
        targetSdk = 34
        versionCode = 3
        versionName = "1.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SPECIALTY_API_BASE", properties.getProperty("SPECIALTY_API_BASE"))
        buildConfigField("String", "RECIPE_API_BASE", properties.getProperty("RECIPE_API_BASE"))
        buildConfigField("String", "SPECIALTY_API_KEY", properties.getProperty("SPECIALTY_API_KEY"))
        buildConfigField("String", "RECIPE_API_KEY", properties.getProperty("RECIPE_API_KEY"))
        buildConfigField("String", "KAKAO_MAP_KEY", properties.getProperty("KAKAO_MAP_KEY"))
        buildConfigField("String", "SEARCH_API_BASE", properties.getProperty("SEARCH_API_BASE"))
        buildConfigField("String", "SEARCH_REST_API_KEY", properties.getProperty("SEARCH_REST_API_KEY"))
        buildConfigField("String", "NAVER_CLIENT_ID", properties.getProperty("NAVER_CLIENT_ID"))
        buildConfigField("String", "NAVER_CLIENT_SECRET", properties.getProperty("NAVER_CLIENT_SECRET"))
        buildConfigField("String", "NAVER_API_BASE", properties.getProperty("NAVER_API_BASE"))
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
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-splashscreen:1.0.0")

    implementation("org.jsoup:jsoup:1.17.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.airbnb.android:lottie:5.2.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.google.android.material:material:1.4.0")
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-firestore")
    // implementation("com.google.firebase:firebase-storage")
    implementation("com.firebaseui:firebase-ui-storage:8.0.2")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    implementation("io.coil-kt:coil:2.6.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    implementation("androidx.activity:activity-ktx:1.1.0")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // To convert xml -> data class
    implementation ("com.tickaroo.tikxml:annotation:0.8.13")
    implementation ("com.tickaroo.tikxml:core:0.8.13")
    implementation ("com.tickaroo.tikxml:retrofit-converter:0.8.13")

    kapt ("com.tickaroo.tikxml:processor:0.8.13")
    // kakao map sdk
    implementation ("com.kakao.maps.open:android:2.9.5")
    // Glid
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    // bottomSheet
    implementation ("com.google.android.material:material:1.12.0")
    //circleindicator
    implementation ("me.relex:circleindicator:2.1.6")
    // 이미지확대 라이브러리
    implementation ("com.davemorrissey.labs:subsampling-scale-image-view:3.10.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
