package com.example.simplerecord.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplerecord.network.RetrofitClient
import com.example.simplerecord.util.AudioRecorderManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

data class TranscriptionState(
    val isRecording: Boolean = false,
    val isLoading: Boolean = false,
    val recordedFilePath: String? = null,
    val transcript: String? = null,
    val error: String? = null
)

class TranscriptionViewModel : ViewModel() {

    private val _transcriptionState = MutableStateFlow(TranscriptionState())
    val transcriptionState: StateFlow<TranscriptionState> = _transcriptionState.asStateFlow()

    private var audioRecorderManager: AudioRecorderManager? = null

    // 初始化录音管理器
    init {
        audioRecorderManager = AudioRecorderManager()
    }

    fun startRecording(cacheDir: String) {
        viewModelScope.launch {
            _transcriptionState.update { it.copy(isRecording = true, recordedFilePath = null, transcript = null, error = null) }
            val filePath = audioRecorderManager?.startRecording(cacheDir)
            if (filePath != null) {
                _transcriptionState.update { it.copy(recordedFilePath = filePath) }
            } else {
                _transcriptionState.update { it.copy(error = "无法开始录音") }
            }
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            audioRecorderManager?.stopRecording()
            _transcriptionState.update { it.copy(isRecording = false) }
            // 文件路径已经在 startRecording 时设置
        }
    }

    fun setRecordedFilePath(path: String) {
        _transcriptionState.update { it.copy(recordedFilePath = path, transcript = null, error = null) }
    }

    fun uploadAudio() {
        val filePath = _transcriptionState.value.recordedFilePath
        if (filePath == null) {
            _transcriptionState.update { it.copy(error = "没有音频文件可供上传") }
            return
        }

        val audioFile = File(filePath)
        if (!audioFile.exists()) {
            _transcriptionState.update { it.copy(error = "音频文件不存在: $filePath") }
            return
        }

        viewModelScope.launch {
            _transcriptionState.update { it.copy(isLoading = true, transcript = null, error = null) }
            try {
                // 根据文件扩展名设置MIME类型
                val mimeType = when (audioFile.extension.lowercase()) {
                    "m4a" -> "audio/mp4"
                    "mp3" -> "audio/mpeg"
                    "wav" -> "audio/wav"
                    "amr" -> "audio/amr"
                    else -> "audio/*"
                }
                val requestFile = audioFile.asRequestBody(mimeType.toMediaTypeOrNull())
                val audioPart = MultipartBody.Part.createFormData("audio_file", audioFile.name, requestFile)

                val response = RetrofitClient.apiService.transcribeAudio(audioPart)
                if (response.isSuccessful) {
                    val transcriptionResult = response.body()
                    _transcriptionState.update {
                        it.copy(
                            isLoading = false,
                            transcript = transcriptionResult?.transcript ?: "无转录结果"
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _transcriptionState.update {
                        it.copy(
                            isLoading = false,
                            error = "转录失败: ${response.code()} ${errorBody ?: response.message()}"
                        )
                    }
                    Log.e("TranscriptionViewModel", "Transcription failed: ${response.code()} $errorBody")
                }
            } catch (e: Exception) {
                _transcriptionState.update { it.copy(isLoading = false, error = "网络错误: ${e.message}") }
                Log.e("TranscriptionViewModel", "Network or transcription error", e)
            } finally {
                // 上传完成后，可以考虑删除临时文件，但保留以供用户再次尝试
                // audioFile.delete()
            }
        }
    }

    /**
     * 从URI获取文件的真实路径。对于通过文件选择器选择的文件，其URI通常不是直接的文件路径。
     * 此方法将URI内容复制到缓存目录并返回新文件的路径。
     */
    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        val fileName = getFileName(context, uri)
        fileName ?: return null

        val tempFile = File(context.cacheDir, fileName)
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return tempFile.absolutePath
        } catch (e: Exception) {
            Log.e("TranscriptionViewModel", "Error copying file from URI", e)
            return null
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        audioRecorderManager?.release()
    }
}
