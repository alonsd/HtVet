buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
    }
} // Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.devtools.ksp") version "1.7.21-1.0.8"
    id("com.google.dagger.hilt.android") version "2.44" apply false
}
