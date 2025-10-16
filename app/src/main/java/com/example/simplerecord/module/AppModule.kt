package com.example.simplerecord.module

import android.content.Context
import androidx.room.Room
import com.example.simplerecord.data.AppDatabase
import com.example.simplerecord.data.BookDao
import com.example.simplerecord.data.BookRepository
import com.example.simplerecord.data.NoteDao
import com.example.simplerecord.data.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepository(noteDao)
    }

    @Provides
    @Singleton
    fun provideBookRepository(bookDao: BookDao): BookRepository {
        return BookRepository(bookDao)
    }


    @Provides
    @Singleton
    fun provideNoteDao(database: AppDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    @Singleton
    fun provideBookDao(database: AppDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "data.db").build()
    }
}