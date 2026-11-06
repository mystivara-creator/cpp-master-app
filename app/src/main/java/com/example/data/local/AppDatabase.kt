package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.DailyChallengeDao
import com.example.data.local.dao.LessonProgressDao
import com.example.data.local.dao.UserProfileDao
import com.example.data.local.entity.DailyChallengeRecordEntity
import com.example.data.local.entity.LessonProgressEntity
import com.example.data.local.entity.UserProfileEntity

@Database(
    entities = [
        LessonProgressEntity::class,
        DailyChallengeRecordEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lessonProgressDao(): LessonProgressDao
    abstract fun dailyChallengeDao(): DailyChallengeDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cpp_master_db"
                )
                    .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
