package com.example.simplerecord.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// 假设的搜索结果数据模型
data class SearchResult(val id: Int, val title: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(emptyList<SearchResult>()) } // 模拟搜索结果

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("搜索") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // 搜索框
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("搜索...") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索图标") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // 模拟搜索按钮 (实际搜索可能在输入时触发)
            Button(
                onClick = {
                    // 这里执行搜索逻辑
                    // 实际应用中，你可能会在这里调用 ViewModel 来执行网络请求等
                    // 为了演示，我们在这里模拟一些结果
                    // For demonstration:
                    if (searchText.isNotBlank()) {
                         searchResults = generateMockResults(searchText) // 实际使用Stateflow或LiveData来更新
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("搜索")
            }

            // 搜索结果列表
            if (searchResults.isEmpty() && searchText.isNotBlank()) {
                Text(
                    text = "没有找到与 \"$searchText\" 相关的结果。",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else if (searchResults.isEmpty() && searchText.isBlank()) {
                Text(
                    text = "请输入关键词进行搜索。",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { result ->
                        SearchResultItem(result = result) {
                            // 处理点击搜索结果项的逻辑
                            println("点击了搜索结果: ${result.title}")
                            // 你可以导航到详情页
                            // navController.navigate("detail/${result.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(result: SearchResult, onClick: (SearchResult) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(result) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = result.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = result.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

// 模拟生成搜索结果 (在实际应用中，这会从 ViewModel/Repository 获取)
fun generateMockResults(query: String): List<SearchResult> {
    val results = mutableListOf<SearchResult>()
    for (i in 1..5) {
        results.add(SearchResult(i, "搜索结果 $i: $query", "这是关于 '$query' 的第 $i 条模拟描述。"))
    }
    return results
}


@Preview
@Composable
fun PreviewSearchScreen() {
    SearchScreen(rememberNavController())
}