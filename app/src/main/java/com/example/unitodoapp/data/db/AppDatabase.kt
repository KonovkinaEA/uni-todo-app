package com.example.unitodoapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.unitodoapp.data.db.entities.ImportanceLevelsDbEntity
import com.example.unitodoapp.data.db.entities.RevisionDbEntity
import com.example.unitodoapp.data.db.entities.TodoDbEntity

@Database(
    version = 1,
    entities = [
        ImportanceLevelsDbEntity::class,
        TodoDbEntity::class,
        RevisionDbEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTodoItemDao(): TodoItemDao

    abstract fun getRevisionDao(): RevisionDao

    companion object {
        private var database: AppDatabase? = null

        fun getDatabaseInstance(context: Context): AppDatabase {
            return if (database == null) {
                synchronized(this) {
                    Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
                        .createFromAsset("todo_database.db")
                        .build()
                }
            } else {
                database!!
            }
        }
    }
}