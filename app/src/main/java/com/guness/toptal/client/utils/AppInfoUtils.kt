package com.guness.toptal.client.utils

val isEspresso: Boolean by lazy {
    try {
        Class.forName("androidx.test.espresso.Espresso")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}

val isRobolectric: Boolean by lazy {
    try {
        Class.forName("org.robolectric.RobolectricTestRunner")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}