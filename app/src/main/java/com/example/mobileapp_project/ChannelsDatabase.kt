package com.example.mobileapp_project

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//  Defines a Room database that holds Channel entities, with the current database schema at version 3
@Database(
    entities = [Channel::class],  // Specifies the data entity classes managed by this database
    version = 3  // Database schema version; incremented to apply new schema changes
)
abstract class ChannelsDatabase : RoomDatabase() {

    // Abstract function to access ChannelDao (Data Access Object) for database operations
    abstract fun dao(): ChannelDao

    companion object {
        // Volatile instance ensures all threads have the latest database instance
        @Volatile
        var INSTANCE: ChannelsDatabase? = null

        // Migration logic from version 2 to version 3 to update schema by adding new columns
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Adds a new column `sensorType` of type TEXT to the `Channel` table
                database.execSQL("ALTER TABLE Channel ADD COLUMN sensorType TEXT")
                // Adds a new column `triggerLevel` of type REAL (floating-point) with a default value of 0.0
                database.execSQL("ALTER TABLE Channel ADD COLUMN triggerLevel REAL DEFAULT 0.0 NOT NULL")
                // Adds a new column `alarmType` of type TEXT to store the alarm type
                database.execSQL("ALTER TABLE Channel ADD COLUMN alarmType TEXT")
            }
        }

        // Singleton instance of the database
        fun getDatabase(context: Context): ChannelsDatabase {
            // Use the existing instance if available; otherwise, create a new database instance
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChannelsDatabase::class.java,  // Database class type
                    "channels.db"  // Database file name
                )
                    .addMigrations(MIGRATION_2_3)  // Applies migration for schema version 2 to 3
                    .build()
                INSTANCE = instance  // Set the new instance to the INSTANCE variable
                instance  // Return the instance
            }
        }
    }
}