import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    id("com.google.devtools.ksp") version "2.3.7"
    id("com.google.dagger.hilt.android")
}

val localPropertiesFile = rootProject.file("local.properties")
val properties = Properties().apply {
    if (localPropertiesFile.isFile) {
        localPropertiesFile.inputStream().use(::load)
    }
}

val apiId = properties.getProperty("api_id")?.trim()?.toIntOrNull() ?: 0
val apiHash = properties.getProperty("api_hash")?.trim().orEmpty()
val escapedApiHash = apiHash
    .replace("\\", "\\\\")
    .replace("\"", "\\\"")

android {
    namespace = "org.gaziz.birgram.core.telegram"
    compileSdk {
        version = release(37)
    }
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField("int", "API_ID", apiId.toString())
        buildConfigField("String", "API_HASH", "\"$escapedApiHash\"")
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    //Android framework
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    //Junit and tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    //Internal modules
    implementation(project(":core:tdlib"))
    //Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
}
