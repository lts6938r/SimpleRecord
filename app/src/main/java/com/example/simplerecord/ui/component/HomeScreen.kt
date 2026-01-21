package com.example.simplerecord.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.simplerecord.ui.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("书籍", "影视", "音乐")
    var menuExpanded by remember { mutableStateOf(false) }

    Column {
        TopAppBar(
            title = { Text("首页") },

            actions = {
                Row {
                    IconButton(onClick = {
                        navController.navigate(Screen.Search.route)
                    }) {
                        Icon(Icons.Default.Search, "Search")
                    }
                    IconButton(onClick = { menuExpanded = !menuExpanded }) {
                        Icon(Icons.Default.Add, "Add")
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "添加书影音",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(5.dp)
                                    )
                                },
                                onClick = {}
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("添加书籍作品") },
                                onClick = {
                                    navController.navigate(Screen.Backup.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("添加影视作品") },
                                onClick = {
                                    navController.navigate(Screen.Transcription.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("添加音乐作品") },
                                onClick = {

                                }
                            )
                        }
                    }
                }
            }
        )
        PrimaryScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
//            containerColor = Color.White,
        ) {
            tabs.forEachIndexed { index, text ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = text) }
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        when (selectedTabIndex) {
            0 -> BooksContent()
            1 -> MoviesContent()
            2 -> MusicContent()
        }
    }
}

@Composable
fun BooksContent() {
    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp, bottom = 65.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),

        ) {

        items(1) {
//            BookCard()
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp, // 设置下划线厚度
                color = Color.LightGray // 设置下划线颜色
            )
        }

    }

}


@Composable
fun MoviesContent() {
    Text("电影内容", modifier = Modifier.padding(16.dp))
}

@Composable
fun MusicContent() {
    Text("音乐内容", modifier = Modifier.padding(16.dp))
}