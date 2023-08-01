plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.facebook.react")
    id("maven-publish")


}

android {
    namespace = "com.example.mylibrary"
    compileSdk = 33

    defaultConfig {
        minSdk = 27

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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


    implementation("com.facebook.react:react-android")
    implementation("com.facebook.react:hermes-android")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation(project(mapOf("path" to ":app")))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}


val yarnInstallCommand by tasks.registering(Exec::class) {
    commandLine("yarn", "install")
    workingDir = file("..") // Set the working directory to the parent directory
}

//// Create a wrapper task to run yarnInstallCommand and publishingPublicationsRelease
//val publishLibrary by tasks.registering {
//    dependsOn(yarnInstallCommand)
//    dependsOn("publishingPublicationsRelease")
//}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.hmarques98"
            artifactId = "my-library"
            version = "1.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

// Use the preBuild lifecycle task to set up the task dependency
tasks.named("preBuild") {
    dependsOn(yarnInstallCommand)
}