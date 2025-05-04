import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

fun loadKeystoreProperties(projectRootDir: File): Properties? {
    val propertiesFile = File(projectRootDir, "keystore.properties")
    return if (propertiesFile.isFile) {
        Properties().apply {
            FileInputStream(propertiesFile).use { fis -> load(fis) }
        }
    } else {
        null // Return null if file doesn't exist (important for CI)
    }
}


android {
    namespace = "dev.bmg.vyasmahabharat"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.bmg.vyasmahabharat"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = project.findProperty("versionNameFromTag")?.toString() ?: "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val keystoreProperties = loadKeystoreProperties(rootProject.rootDir)

    signingConfigs {
        create("release") {
            if (keystoreProperties != null) {
                // Read from properties file if it exists
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            } else {
                // Read from environment variables (for CI/GitHub Actions)
                // We'll set these env vars in the GitHub Actions workflow
                storeFile = System.getenv("SIGNING_STORE_FILE")?.let { file(it) }
                storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
            }
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = true // Recommended for release
            isShrinkResources = true // Recommended for release
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Apply the signing configuration to the release build type
            signingConfig = signingConfigs.getByName("release")

        }
        debug {
            isMinifyEnabled = false // Recommended for release
            isShrinkResources = false // Recommended for release
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    applicationVariants.all {
        val variant = this
        if (variant.buildType.name == "release") {
            variant.outputs.all {
                // Cast to ApkVariantOutputImpl for newer AGP versions
                (this as com.android.build.gradle.internal.api.ApkVariantOutputImpl).apply {
                    val projectBaseName = "vyas-mahabharat"
                    val versionName = variant.versionName
                    outputFileName = "${projectBaseName}-v${versionName}.apk"
                    println("Set output file name to: $outputFileName")
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src/main/assets")
            }
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // JSON parsing
    implementation(libs.gson)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}