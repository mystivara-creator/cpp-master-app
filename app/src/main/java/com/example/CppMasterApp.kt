package com.example

import android.app.Application
import android.util.Log
import com.example.data.auth.AuthManager
import com.google.firebase.FirebaseApp

class CppMasterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
            // Safely initialize Auth in the background
            AuthManager.initAuth(this)
        } catch (e: Throwable) {
            Log.e("CppMasterApp", "Inisialisasi Firebase aman: ${e.message}")
        }
    }
}
