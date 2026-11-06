package com.example.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import java.io.File

object SystemIntegrationHelper {
    const val CHANNEL_ID = "cpp_master_study_channel"
    private const val NOTIFICATION_ID = 1001
    private const val WORKSPACE_DIR = "cpp_workspace"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pengingat Belajar C++"
            val descriptionText = "Notifikasi pengingat tantangan harian dan progres materi C++"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun isNotificationPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }

    fun isStoragePermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ uses zero-permission scoped workspace & Storage Access Framework
            true
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun sendStudyNotification(
        context: Context,
        title: String = "C++ Master Siap Digunakan",
        message: String = "Lanjutkan latihan tantangan harian C++20 untuk menjaga streak belajarmu."
    ): Boolean {
        if (!isNotificationPermissionGranted(context)) return false
        return try {
            createNotificationChannel(context)
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
            true
        } catch (_: SecurityException) {
            false
        } catch (_: Throwable) {
            false
        }
    }

    fun getWorkspaceDirectory(context: Context): File {
        val baseDir = context.getExternalFilesDir(WORKSPACE_DIR)
            ?: File(context.filesDir, WORKSPACE_DIR)
        if (!baseDir.exists()) {
            baseDir.mkdirs()
        }
        return baseDir
    }

    fun saveCppFileToWorkspace(context: Context, fileName: String, code: String): File? {
        return try {
            val sanitizedName = if (fileName.endsWith(".cpp")) fileName else "$fileName.cpp"
            val dir = getWorkspaceDirectory(context)
            val target = File(dir, sanitizedName)
            target.writeText(code)
            target
        } catch (_: Throwable) {
            null
        }
    }

    fun getSavedWorkspaceFilesCount(context: Context): Int {
        return try {
            val dir = getWorkspaceDirectory(context)
            dir.listFiles { file -> file.isFile && file.name.endsWith(".cpp") }?.size ?: 0
        } catch (_: Throwable) {
            0
        }
    }

    fun writeCodeToUri(context: Context, uri: Uri, code: String): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(code.toByteArray(Charsets.UTF_8))
                outputStream.flush()
            }
            true
        } catch (_: Throwable) {
            false
        }
    }

    fun readCodeFromUri(context: Context, uri: Uri): String? {
        return try {
            context.contentResolver.openInputStream(uri)?.bufferedReader(Charsets.UTF_8)?.use {
                it.readText()
            }
        } catch (_: Throwable) {
            null
        }
    }
}
