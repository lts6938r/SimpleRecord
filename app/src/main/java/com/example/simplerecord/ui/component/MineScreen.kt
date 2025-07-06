package com.example.simplerecord.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.simplerecord.ui.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MineScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // 轻微的背景色区分
        ) {



            // --- 2. 常用功能入口 ---
            // 使用 Card 包裹可以提供更好的视觉分离和阴影效果
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    MineMenuItem(
                        icon = Icons.Default.Star,
                        title = "我的收藏",
                        onClick = { /* TODO: Navigate to Favorites screen */ }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- 3. 服务与支持 ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    MineMenuItem(
                        icon = Icons.Default.Info,
                        title = "帮助与反馈",
                        onClick = { /* TODO: Navigate to Help/Feedback screen */ }
                    )
                    // ... 可以添加更多服务支持项
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            // --- 4. 设置 (底部) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    MineMenuItem(
                        icon = Icons.Default.Settings,
                        title = "设置",
                        onClick = { navController.navigate(Screen.Settings.route) }
                    )
                    MineMenuItem(
                        icon = Icons.Default.Notifications,
                        title = "通知设置",
                        onClick = { /* TODO: Navigate to Notification Settings screen */ }
                    )

                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // 底部留白
        }
    }
}



@Composable
fun MineMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // 使内容两端对齐
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null, // 通常图标不需要 contentDescription
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "进入",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}


@Preview
@Composable
fun PreviewMineScreen() {
    MineScreen(rememberNavController())
}