plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {

    namespace = "mago.apps.wearhealthrawdata"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "mago.apps.wearhealthrawdata"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
    }


    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = AppConfig.javaVersion
        targetCompatibility = AppConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.composeCompiler
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))

    Libraries.apply {
        Libraries.KTX.apply {
            implementation(core)
        }

        Libraries.Compose.apply {
            implementation(material)
            implementation(activity)
            implementation(ui)
            implementation(uiTooling)
            implementation(navigation)
            implementation(constraintLayout)
            implementation(wearMaterial)
            implementation(wearFoundation)
        }
    }
}