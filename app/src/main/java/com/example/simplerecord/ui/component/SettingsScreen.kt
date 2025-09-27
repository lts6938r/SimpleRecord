package com.example.simplerecord.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.simplerecord.data.SettingsManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) } // 在这里获取 SettingsManager 实例

    val isDarkModeEnabled by settingsManager.isDarkModeEnabled.collectAsState(initial = false)

    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White // Consistent with HomeScreen
                ),
                // You could add actions here if needed, e.g., a "Help" icon
                actions = {
                    // IconButton(onClick = { /* Handle help click */ }) {
                    //     Icon(Icons.Default.HelpOutline, "帮助")
                    // }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SettingsSectionTitle("通用设置")
            }
            item {
                SwitchSettingItem(
                    title = "启用通知",
                    description = "接收应用通知和提醒",
                    initialChecked = true,
                    onCheckedChange = { isChecked ->
                        // Handle notification setting change
                        println("启用通知: $isChecked")
                    }
                )
            }
            item {
                ClickableSettingItem(
                    title = "语言",
                    description = "简体中文",
                    onClick = {
                        // Navigate to language selection screen
                        println("点击语言设置")
                    }
                )
            }
            item {
                ClickableSettingItem(
                    title = "主题模式",
                    description = "跟随系统",
                    onClick = {
                        // Show theme selection dialog/screen
                        println("点击主题模式设置")
                    }
                )
            }

            // Privacy Settings Section
            item {
                SettingsSectionTitle("隐私设置")
            }
//            Switch(
//                checked = isDarkModeEnabled,
//                onCheckedChange = { newMode ->
//                    coroutineScope.launch {
//                        settingsManager.setDarkModeEnabled(newMode)
//                    }
//                }
//            )
            item {
                SwitchSettingItem(
                    title = "个性化广告",
                    description = "根据您的兴趣展示个性化广告",
                    initialChecked = isDarkModeEnabled,
                    onCheckedChange = { isChecked ->
                        coroutineScope.launch {
                            settingsManager.setDarkModeEnabled(isChecked)
                        }
                    }
                )
            }
            item {
                ClickableSettingItem(
                    title = "数据与隐私",
                    onClick = {
                        // Navigate to data & privacy details
                        println("点击数据与隐私")
                    }
                )
            }

            // Account Settings Section
            item {
                SettingsSectionTitle("账户设置")
            }
            item {
                ClickableSettingItem(
                    title = "账户信息",
                    onClick = {
                        // Navigate to account info screen
                        println("点击账户信息")
                    }
                )
            }
            item {
                ClickableSettingItem(
                    title = "修改密码",
                    onClick = {
                        // Navigate to change password screen
                        println("点击修改密码")
                    }
                )
            }

            // About Section
            item {
                SettingsSectionTitle("关于")
            }
            item {
                ClickableSettingItem(
                    title = "版本信息",
                    description = "1.0.0 (Build 20250602)",
                    onClick = {
                        // Show detailed version info or changelog
                        println("点击版本信息")
                    }
                )
            }
            item {
                ClickableSettingItem(
                    title = "用户协议",
                    onClick = {
                        // Navigate to user agreement document
                        println("点击用户协议")
                    }
                )
            }
            item {
                ClickableSettingItem(
                    title = "隐私政策",
                    onClick = {
                        // Navigate to privacy policy document
                        println("点击隐私政策")

                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp)) // Add some space at the bottom
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "深色模式")
                    Switch(
                        checked = isDarkModeEnabled,
                        onCheckedChange = { newMode ->
                            coroutineScope.launch {
                                settingsManager.setDarkModeEnabled(newMode)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall, // Use Material 3 typography
        color = MaterialTheme.colorScheme.primary, // Use Material 3 color scheme
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun SwitchSettingItem(
    title: String,
    description: String? = null,
    initialChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    var isChecked by remember { mutableStateOf(initialChecked) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isChecked = !isChecked
                    onCheckedChange(isChecked)
                }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                ) // Material 3 typography
                description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall, // Material 3 typography
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Switch(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    onCheckedChange(it)
                }
            )
        }
        HorizontalDivider() // Material 3 Divider
    }
}

@Composable
fun ClickableSettingItem(
    title: String,
    description: String? = null,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                ) // Material 3 typography
                description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall, // Material 3 typography
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

        }
        HorizontalDivider()
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen(rememberNavController())
}