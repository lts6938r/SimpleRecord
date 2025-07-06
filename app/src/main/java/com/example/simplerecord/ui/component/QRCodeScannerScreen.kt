package com.example.simplerecord.ui.component

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

@SuppressLint("ContextCastToActivity")
@Composable
fun QRCodeScannerScreen() {
    val context = LocalContext.current as Activity

    // 创建一个ActivityResultLauncher来启动扫描Activity并处理结果
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intentResult: IntentResult? =
                IntentIntegrator.parseActivityResult(result.resultCode, result.data)
            intentResult?.contents?.let { contents ->
                // 扫描成功，contents就是二维码/条形码的内容
                Toast.makeText(context, "扫描结果: $contents", Toast.LENGTH_LONG).show()
                // 可以在这里更新UI或者导航到其他屏幕
            } ?: run {
                // 用户取消扫描
                Toast.makeText(context, "扫描取消", Toast.LENGTH_SHORT).show()
            }
        } else {
            // 扫描失败或其他情况
            Toast.makeText(context, "扫描失败或取消", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            // 实例化IntentIntegrator并配置扫描选项
            val integrator = IntentIntegrator(context)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) // 只扫描二维码
            integrator.setPrompt("请将二维码放入框内") // 扫描界面提示
            integrator.setCameraId(0) // 使用后置摄像头
            integrator.setBeepEnabled(false) // 扫描成功后播放声音
            integrator.setBarcodeImageEnabled(false) // 扫描成功后保存图片
            integrator.setOrientationLocked(false) // 不锁定屏幕方向
            // 创建扫描Intent
            val scanIntent: Intent = integrator.createScanIntent()
            // 启动扫描Activity
            scanLauncher.launch(scanIntent)
        }) {
            Text("开始扫描二维码")
        }
    }
}