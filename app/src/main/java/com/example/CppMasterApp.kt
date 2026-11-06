package com.example

import android.app.Application
import android.util.Log
import com.example.data.auth.AuthManager
import com.example.util.SystemIntegrationHelper
import com.google.firebase.FirebaseApp

class CppMasterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            SystemIntegrationHelper.createNotificationChannel(this)
            SystemIntegrationHelper.getWorkspaceDirectory(this)
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
            AuthManager.initAuth(this)
        } catch (e: Throwable) {
            Log.e("CppMasterApp", "Inisialisasi awal aplikasi aman: ${e.message}")
        }
    }
}
