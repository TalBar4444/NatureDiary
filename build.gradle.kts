// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.0" apply false
    //Google services Gradle plugin dependency
    id("com.google.gms.google-services") version "4.4.1" apply false

    //Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}