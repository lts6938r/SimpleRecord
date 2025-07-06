package com.example.simplerecord.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BookCard() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { /* Handle click event, e.g., navigate to book detail */ }
        .padding(8.dp) // Add padding to the row
    ) {
        Column { // Added start padding
            Text(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = "三体" // Placeholder text
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text("中国", fontSize = 14.sp, color = Color.Gray)
                Text(" / ", fontSize = 14.sp, color = Color.Gray)
                Text("刘慈欣", fontSize = 14.sp, color = Color.Gray)
                Text(" / ", fontSize = 14.sp, color = Color.Gray)
                Text("科幻", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}