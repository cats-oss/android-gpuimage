apply plugin: 'com.android.application'
apply plugin: 'kotlin-android-extensions'
apply plugin: 'kotlin-android'

android {
    compileSdkVersion COMPILE_SDK_VERSION as int

    defaultConfig {
        minSdkVersion MIN_SDK_VERSION as int
        targetSdkVersion TARGET_SDK_VERSION as int

        versionCode = VERSION_CODE as int
        versionName = VERSION_NAME
    }

    buildTypes {
        debug {
            debuggable true
        }
        release {
            debuggable false
            zipAlignEnabled true
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
repositories {
//     maven { url = "https://oss.sonatype.org/content/repositories/snapshots"}
}

dependencies {
    implementation project(':library')
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
}