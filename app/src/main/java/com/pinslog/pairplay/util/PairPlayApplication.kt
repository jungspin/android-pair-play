package com.pinslog.pairplay.util

import android.app.Application
import android.content.Intent
import android.os.Process
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore
import com.google.firebase.ktx.Firebase
import com.pinslog.pairplay.view.ErrorActivity
import kotlin.system.exitProcess

class PairPlayApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        Thread.setDefaultUncaughtExceptionHandler { _, throwable -> caughtException(throwable) }
    }

    private fun caughtException(throwable: Throwable) {
        // record exception
        FirebaseCrashlytics.getInstance().recordException(throwable)
        // start error activity
        startErrorActivity()
        // kill process
        Process.killProcess(Process.myPid())
        exitProcess(-1)
    }

    private fun startErrorActivity(){
        val intent = Intent(this, ErrorActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}