package com.example.simplerecord.ui.component

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplerecord.viewmodel.TranscriptionViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TranscriptionScreen(viewModel: TranscriptionViewModel = viewModel()) {
    val context = LocalContext.current
    val transcriptionState by viewModel.transcriptionState.collectAsState()

    // Android 13+ 需要 READ_MEDIA_AUDIO，旧版本需要 READ_EXTERNAL_STORAGE
    val permissionsToRequest = remember {
        mutableListOf(Manifest.permission.RECORD_AUDIO).apply {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.READ_MEDIA_AUDIO)
            } else {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    val permissionState = rememberMultiplePermissionsState(permissionsToRequest.toList())

    // 文件选择器
    val pickAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val filePath = viewModel.getFilePathFromUri(context, it)
            if (filePath != null) {
                viewModel.setRecordedFilePath(filePath)
                Toast.makeText(
                    context,
                    "Selected: ${filePath.substringAfterLast('/')}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(context, "Failed to get file path from URI", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    LaunchedEffect(Unit) {
        // 请求权限
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "音频转录",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 录音按钮
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (permissionState.allPermissionsGranted) {
                        viewModel.startRecording(context.cacheDir.absolutePath)
                        Toast.makeText(context, "开始录音...", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "请授予录音权限", Toast.LENGTH_SHORT).show()
                        permissionState.launchMultiplePermissionRequest()
                    }
                },
                enabled = !transcriptionState.isRecording && !transcriptionState.isLoading
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "开始录音")
                Spacer(Modifier.width(8.dp))
                Text("录音")
            }

            Button(
                onClick = {
                    viewModel.stopRecording()
                    Toast.makeText(context, "录音结束", Toast.LENGTH_SHORT).show()
                },
                enabled = transcriptionState.isRecording && !transcriptionState.isLoading
            ) {
                Icon(Icons.Filled.Close, contentDescription = "停止录音")
                Spacer(Modifier.width(8.dp))
                Text("停止")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 文件路径显示
        val recordedFile = transcriptionState.recordedFilePath
        if (recordedFile != null) {
            Text(
                "录音文件: ${recordedFile.substringAfterLast('/')}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Text("未录制音频或未选择文件", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 选择文件按钮
        Button(
            onClick = {
                if (permissionState.allPermissionsGranted) {
                    pickAudioLauncher.launch("audio/*") // Mime type for audio files
                } else {
                    Toast.makeText(context, "请授予存储权限", Toast.LENGTH_SHORT).show()
                    permissionState.launchMultiplePermissionRequest()
                }
            },
            enabled = !transcriptionState.isRecording && !transcriptionState.isLoading
        ) {
            Text("选择本地音频文件")
        }


        Spacer(modifier = Modifier.height(24.dp))

        // 转录按钮
        Button(
            onClick = {
                if (transcriptionState.recordedFilePath != null) {
                    viewModel.uploadAudio()
                } else {
                    Toast.makeText(context, "请先录音或选择一个音频文件", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !transcriptionState.isLoading && !transcriptionState.isRecording && transcriptionState.recordedFilePath != null
        ) {
            Icon(Icons.Filled.Add, contentDescription = "转录")
            Spacer(Modifier.width(8.dp))
            Text("转录音频")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 状态指示
        if (transcriptionState.isRecording) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Text(
                "正在录音...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else if (transcriptionState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            Text(
                "正在转录，请稍候...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            transcriptionState.transcript?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("转录结果:", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(it, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        // 错误信息显示
        transcriptionState.error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "错误: $it",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
