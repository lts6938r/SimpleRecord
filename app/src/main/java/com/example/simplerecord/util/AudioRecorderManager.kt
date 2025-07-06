package com.example.simplerecord.util

import android.media.MediaRecorder
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 封装 Android MediaRecorder 的录音管理类
 */
class AudioRecorderManager {

    private var mediaRecorder: MediaRecorder? = null
    private var audioFilePath: String? = null

    /**
     * 开始录音
     * @param cacheDir 缓存目录路径
     * @return 录音文件的绝对路径，如果失败则返回 null
     */
    fun startRecording(cacheDir: String): String? {
        if (mediaRecorder != null) {
            Log.w("AudioRecorderManager", "Recorder is already active, stopping before new recording.")
            stopRecording() // 确保在开始新录音前停止旧录音
        }

        // 生成唯一的文件名
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "audio_${timestamp}.mp4" // 使用.mp4 (MPEG-4/AAC) 作为通用格式
        val file = File(cacheDir, fileName)
        audioFilePath = file.absolutePath

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // 设置输出格式为 MPEG_4 (AAC)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)    // 设置音频编码器为 AAC
            setOutputFile(audioFilePath)
            try {
                prepare()
                start()
                Log.d("AudioRecorderManager", "Recording started: $audioFilePath")
            } catch (e: IOException) {
                Log.e("AudioRecorderManager", "prepare() failed", e)
                release() // 释放资源
                audioFilePath = null
            } catch (e: IllegalStateException) {
                Log.e("AudioRecorderManager", "start() failed: Illegal state", e)
                release() // 释放资源
                audioFilePath = null
            }
        }
        return audioFilePath
    }

    /**
     * 停止录音
     */
    fun stopRecording() {
        mediaRecorder?.apply {
            try {
                stop()
                release() // 释放 MediaRecorder 资源
                Log.d("AudioRecorderManager", "Recording stopped. File: $audioFilePath")
            } catch (e: IllegalStateException) {
                Log.e("AudioRecorderManager", "stop() failed: Illegal state", e)
            } catch (e: RuntimeException) {
                // This can happen if stop() is called too quickly after start()
                Log.e("AudioRecorderManager", "stop() failed: RuntimeException (e.g., no valid audio data)", e)
            } finally {
                mediaRecorder = null
            }
        }
    }

    /**
     * 释放 MediaRecorder 资源
     */
    fun release() {
        mediaRecorder?.release()
        mediaRecorder = null
        Log.d("AudioRecorderManager", "MediaRecorder released.")
    }
}