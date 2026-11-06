package com.example.data.auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object AuthManager {
    private const val TAG = "AuthManager"

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    @Volatile
    private var isListenerAttached = false

    fun initAuth(context: Context) {
        try {
            ensureFirebase(context)
            val auth = Firebase.auth
            _currentUser.value = auth.currentUser
            if (!isListenerAttached) {
                auth.addAuthStateListener { a ->
                    _currentUser.value = a.currentUser
                }
                isListenerAttached = true
            }
        } catch (e: Throwable) {
            Log.w(TAG, "FirebaseAuth belum dapat diakses saat ini: ${e.message}")
        }
    }

    private fun ensureFirebase(context: Context) {
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
        } catch (e: Throwable) {
            Log.e(TAG, "Gagal menginisialisasi FirebaseApp: ${e.message}")
        }
    }

    private tailrec fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

    fun onGoogleSignInClicked(
        context: Context,
        credentialManager: CredentialManager,
        scope: CoroutineScope,
        onSuccess: (FirebaseUser) -> Unit,
        onError: (String) -> Unit,
        onCancelled: () -> Unit = {}
    ) {
        ensureFirebase(context)

        val activity = context.findActivity()
        if (activity == null) {
            onError("Konteks layar tidak ditemukan untuk menampilkan dialog login Google.")
            return
        }

        val resId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
        if (resId == 0) {
            onError("Konfigurasi Google Services belum aktif (default_web_client_id tidak ditemukan).")
            return
        }
        val clientId = context.getString(resId)

        val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(signInOption).build()

        scope.launch {
            try {
                val result = credentialManager.getCredential(activity, request)
                val credential = result.credential
                if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                    val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                    val authResult = withContext(Dispatchers.IO) {
                        Firebase.auth.signInWithCredential(authCredential).await()
                    }
                    val user = authResult.user
                    if (user != null) {
                        _currentUser.value = user
                        withContext(Dispatchers.IO) {
                            syncUserToFirestore(context, user)
                        }
                        onSuccess(user)
                    } else {
                        onError("Gagal mendapatkan akun pengguna.")
                    }
                } else {
                    onError("Tipe kredensial tidak sesuai.")
                }
            } catch (e: GetCredentialCancellationException) {
                Log.w(TAG, "Google Sign-In dibatalkan oleh pengguna", e)
                onCancelled()
            } catch (e: Throwable) {
                Log.e(TAG, "Google Sign-In gagal", e)
                onError(e.localizedMessage ?: "Gagal masuk dengan akun Google")
            }
        }
    }

    fun signOut(
        context: Context,
        credentialManager: CredentialManager,
        scope: CoroutineScope,
        onComplete: () -> Unit
    ) {
        try {
            ensureFirebase(context)
            Firebase.auth.signOut()
            _currentUser.value = null
        } catch (e: Throwable) {
            Log.e(TAG, "Gagal sign out dari Firebase: ${e.message}")
        }
        scope.launch {
            try {
                withContext(Dispatchers.IO) {
                    credentialManager.clearCredentialState(ClearCredentialStateRequest())
                }
            } catch (e: Throwable) {
                Log.e(TAG, "Gagal membersihkan status kredensial", e)
            } finally {
                onComplete()
            }
        }
    }

    private suspend fun syncUserToFirestore(context: Context, user: FirebaseUser) {
        try {
            ensureFirebase(context)
            val dbResId = context.resources.getIdentifier("firestore_database_id", "string", context.packageName)
            val db = if (dbResId != 0) {
                FirebaseFirestore.getInstance(context.getString(dbResId))
            } else {
                FirebaseFirestore.getInstance()
            }
            val userDoc = db.collection("users").document(user.uid)
            val data = mapOf(
                "userId" to user.uid,
                "displayName" to (user.displayName ?: "Pelajar C++"),
                "email" to (user.email ?: ""),
                "photoUrl" to (user.photoUrl?.toString() ?: ""),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            userDoc.set(data, SetOptions.merge()).await()
        } catch (e: Throwable) {
            Log.e(TAG, "Gagal sinkronisasi data user ke Firestore", e)
        }
    }
}
