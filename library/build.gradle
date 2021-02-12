apply plugin: 'com.android.library'

android {
    compileSdkVersion COMPILE_SDK_VERSION as int

    defaultConfig {
        minSdkVersion MIN_SDK_VERSION as int
        targetSdkVersion TARGET_SDK_VERSION as int

        versionCode = VERSION_CODE as int
        versionName = VERSION_NAME
        ndk.abiFilters 'armeabi-v7a','arm64-v8a','x86','x86_64'
        externalNativeBuild {
            cmake { cppFlags "" }
        }
    }
    externalNativeBuild {
        cmake { path "src/main/cpp/CMakeLists.txt" }
    }

    buildTypes {
        debug {
            debuggable true
        }
        release {
            debuggable false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

ext {
    bintrayRepo = 'maven'
    bintrayName = 'gpuimage'
    bintrayUserOrg = 'cats-oss'
    publishedGroupId = 'jp.co.cyberagent.android'
    libraryName = 'gpuimage'
    artifact = 'gpuimage'
    libraryDescription = 'Image filters for Android with OpenGL (based on GPUImage for iOS)'
    siteUrl = 'https://github.com/cats-oss/android-gpuimage'
    gitUrl = 'https://github.com/cats-oss/android-gpuimage.git'
    issueUrl = 'https://github.com/cats-oss/android-gpuimage/issues'
    libraryVersion = VERSION_NAME
    developerId = 'cats'
    developerName = 'CATS'
    developerEmail = 'dadadada.chop@gmail.com'
    licenseName = 'The Apache Software License, Version 2.0'
    licenseUrl = 'http://www.apache.org/licenses/LICENSE-2.0.txt'
    allLicenses = ["Apache-2.0"]
}

// TODO: Close JCenter on May 1st https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/
// apply from: 'https://gist.githubusercontent.com/wasabeef/cf14805bee509baf7461974582f17d26/raw/bintray-v1.gradle'
// apply from: 'https://gist.githubusercontent.com/wasabeef/cf14805bee509baf7461974582f17d26/raw/install-v1.gradle'

apply from: 'https://gist.githubusercontent.com/wasabeef/2f2ae8d97b429e7d967128125dc47854/raw/maven-central-v1.gradle'