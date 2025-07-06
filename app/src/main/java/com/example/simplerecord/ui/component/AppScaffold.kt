package com.example.simplerecord.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.simplerecord.model.Note
import com.example.simplerecord.ui.navigation.AppNavHost
import com.example.simplerecord.ui.navigation.Screen
import com.example.simplerecord.viewmodel.NoteViewModel

@Composable
fun AppScaffold(
    navController: NavHostController,
    noteViewModel: NoteViewModel,
    notes: List<Note>
) {
    val bottomNavigationItems = listOf(
        Pair("首页", Icons.Filled.Home) to Screen.Home.route,
        Pair("笔记", Icons.Filled.Edit) to Screen.Notes.route,
        Pair("我的", Icons.Filled.Person) to Screen.Mine.route
    )
    // 监听当前的导航回栈入口，以获取当前路由
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // 判断是否显示底部导航栏的逻辑
    // 如果当前路由在 bottomNavigationItems 的路由列表中，则显示
    val shouldShowBottomBar = bottomNavigationItems.any { (pair, route) ->
        // 使用 hierarchy 检查当前目的地是否是某个根目的地或其子目的地
        currentDestination?.hierarchy?.any { it.route == route } == true
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface // 使用主题的表面颜色，更符合 Material Design 规范
                ) {
                    bottomNavigationItems.forEach { (item, route) ->
                        val (label, icon) = item // 解构 Pair
                        val isSelected = currentDestination?.hierarchy?.any { it.route == route } == true

                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = label) }, // contentDescription 应该有意义
                            label = { Text(label) },
                            selected = isSelected,
                            onClick = {
                                // 如果点击的不是当前选中的项，则导航
                                if (!isSelected) {
                                    navController.navigate(route) {
                                        // 弹出到导航图的起始目的地，保存其状态
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // 避免多次创建同一目的地的实例
                                        launchSingleTop = true
                                        // 在重新选择之前选中的项时恢复其状态
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        // AppNavHost 应该接收 paddingValues 以便内容不会被底部导航栏遮挡
        AppNavHost(
            navController = navController,
            noteViewModel = noteViewModel,
            notes = notes,
            innerPadding = paddingValues // 将 Scaffold 提供的内边距传递给 NavHost
        )
    }
}