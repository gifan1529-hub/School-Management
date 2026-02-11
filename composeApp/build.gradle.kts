import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation("io.insert-koin:koin-android:4.0.0")
            implementation("io.ktor:ktor-client-okhttp:3.0.0")
        }
        commonMain.dependencies {
            val ktorVersion = "3.0.0"
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // VIEWMODEL
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel:2.8.2")

            // ROOM
            implementation("androidx.room:room-runtime:2.7.0-alpha11")
            implementation("androidx.sqlite:sqlite-bundled:2.5.0-alpha01")

            // KTOR
            implementation("io.ktor:ktor-client-core:${ktorVersion}")
            implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
            implementation("io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}")
            implementation("io.ktor:ktor-client-logging:${ktorVersion}")
            implementation("io.ktor:ktor-client-encoding:${ktorVersion}")

            implementation("io.ktor:ktor-client-serialization:${ktorVersion}")

            implementation("com.russhwolf:multiplatform-settings-no-arg:1.1.1")

            implementation("com.squareup.okio:okio:3.9.0")
            implementation("androidx.datastore:datastore-preferences-core:1.1.1")

            // Nav DeepLink
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha08")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:2.8.0")

            implementation(compose.components.resources)

            // Koin
            implementation("io.insert-koin:koin-core:4.0.0")
            implementation("io.insert-koin:koin-compose:4.0.0")
            implementation("io.insert-koin:koin-compose-viewmodel:4.0.0")

            implementation(compose.materialIconsExtended)

            implementation("io.github.aakira:napier:2.7.1")

            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

            // date
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

            // scan
            implementation(libs.qr.kit)

            // permission
            implementation(libs.moko.permissions)

            // location
            implementation("com.google.android.gms:play-services-location:21.3.0")

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")

            // file picker
            implementation("com.darkrockstudios:mpfilepicker:3.1.0")

//            // library toast
//            implementation(libs.kmp.notifier)
        }
        iosMain.dependencies{
            implementation("io.ktor:ktor-client-darwin:3.0.0")
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.example.schoolmanagement"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.schoolmanagement"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

