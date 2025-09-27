package com.example.simplerecord.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun FixedSizeNetworkImage(imageUrl: String) {

    val fixedSize = 120.dp

    AsyncImage(
        model = imageUrl,
        contentDescription = "固定尺寸的网络图片",

        modifier = Modifier
            .size(fixedSize),

        // 推荐使用 ContentScale.Crop 来裁剪图片以完全填充固定区域
        // 或使用 ContentScale.Fit 来在固定区域内完整显示图片（可能留白）
        contentScale = ContentScale.Fit,

        // 其它可选参数：
        // placeholder = painterResource(R.drawable.loading_placeholder),
        // error = painterResource(R.drawable.error_image)
    )
}