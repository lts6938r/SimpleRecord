package com.example.simplerecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.lifecycle.lifecycleScope
import com.example.simplerecord.data.BookRepository
import kotlinx.coroutines.launch
import com.example.simplerecord.model.Book

class SecondActivity : ComponentActivity() {
    private lateinit var bookRepository: BookRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val application = application as MyApplication
        bookRepository = application.bookRepository

        setContent {
            Column {
                Button(onClick = {

                    println("Database is managed by Room.")
                }) {
                    Text(text = "创建数据库 (Room已管理)")
                }
                Button(onClick = {
                    lifecycleScope.launch {
                        // Example of inserting data using the Room BookRepository
                        val book1 = Book(name = "The Hitchhiker's Guide to the Galaxy", author = "Douglas Adams", country = "UK", category = "Sci-Fi", comment = "Hilarious!")
                        val book2 = Book(name = "1984", author = "George Orwell", country = "UK", category = "Dystopian", comment = "A classic warning.")
                        val book3 = Book(name = "The Lord of the Rings", author = "J.R.R. Tolkien", country = "UK", category = "Fantasy", comment = "Epic adventure.")

                        bookRepository.insertBook(book1)
                        bookRepository.insertBook(book2)
                        bookRepository.insertBook(book3)
                        println("Books inserted!")
                    }
                }) {
                    Text(text = "插入数据")
                }
            }
        }
    }
}