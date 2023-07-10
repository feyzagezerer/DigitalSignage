package com.fey.signage

import android.app.Application
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SignageApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (!isRoboUnitTest()) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun isRoboUnitTest(): Boolean {
        return "robolectric" == Build.FINGERPRINT
    }

}