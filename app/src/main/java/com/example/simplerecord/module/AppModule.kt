package com.example.simplerecord.module

import android.content.Context
import androidx.room.Room
import com.example.simplerecord.data.AppDatabase
import com.example.simplerecord.data.BookDao
import com.example.simplerecord.data.BookRepository
import com.example.simplerecord.data.NoteDao
import com.example.simplerecord.data.NoteRepository
import com.example.simplerecord.BuildConfig
import com.example.simplerecord.api.AudioApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://192.168.123.127:8000/"

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

    @Provides
    @Singleton
    fun provideNetworkJson(): Json {
        return Json {
            ignoreUnknownKeys = true // Best practice for APIs to avoid crashes on new fields
            coerceInputValues = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, networkJson: Json): Retrofit {
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // 1. 创建日志拦截器
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // 仅在 Debug 模式下打印日志，Release 模式关闭以保护数据安全
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        // 2. 构建 OkHttpClient
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            // .addInterceptor(AuthInterceptor()) // 如果有 Token 验证，通常在这里添加自定义拦截器
            .connectTimeout(15, TimeUnit.SECONDS) // 连接超时
            .readTimeout(15, TimeUnit.SECONDS)    // 读取超时
            .writeTimeout(15, TimeUnit.SECONDS)   // 写入超时
            .retryOnConnectionFailure(true)       // 失败重连
            .build()
    }

    @Provides
    @Singleton
    fun provideAudioApiService(retrofit: Retrofit): AudioApiService {
        return retrofit.create(AudioApiService::class.java)
    }
}