package com.example.simplerecord.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplerecord.viewmodel.BookViewModel

@Composable
fun BookScreen(
    bookViewModel: BookViewModel,

    ) {
    val books by bookViewModel.bookList.collectAsStateWithLifecycle()
    val isLoading by bookViewModel.isLoading.collectAsStateWithLifecycle()
    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        books.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "没有找到数据。")
            }
        }

        else -> {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = 65.dp,
                    start = 12.dp,
                    end = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),

                ) {

                items(books) { book ->
                    BookItem(
                        name = book.name,
                        author = book.author,
                        country = book.country,
                        category = book.category,
                        cover = book.cover,
                        year = book.year
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }

            }
        }
    }

}

@Composable
fun BookItem(
    name: String,
    author: String,
    country: String,
    category: String,
    cover: String,
    year: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(8.dp)
    ) {

        FixedSizeNetworkImage(cover)
        Spacer(modifier = Modifier.width(30.dp))
        Column {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = name
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(country, fontSize = 14.sp, color = Color.Gray)
                Text(" / ", fontSize = 14.sp, color = Color.Gray)
                Text(author, fontSize = 14.sp, color = Color.Gray)
                Text(" / ", fontSize = 14.sp, color = Color.Gray)
                Text(category, fontSize = 14.sp, color = Color.Gray)
                Text(" / ", fontSize = 14.sp, color = Color.Gray)
                Text(year, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}