package com.example.simplerecord.data

import android.content.Context

import androidx.room.Database

import androidx.room.Room

import androidx.room.RoomDatabase

import com.example.simplerecord.model.Book

import com.example.simplerecord.model.Note


@Database(entities = [Note::class, Book::class], version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun bookDao(): BookDao

//    companion object {
//
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//        fun getDatabase(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "Data.db"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}