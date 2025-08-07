package com.helloumi.data.datasource.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.helloumi.data.datasource.local.dao.ContactDao
import com.helloumi.data.datasource.local.entity.ContactEntity

@Database(
    entities = [ContactEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null

        fun getDatabase(context: Context): ContactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    "contact_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
