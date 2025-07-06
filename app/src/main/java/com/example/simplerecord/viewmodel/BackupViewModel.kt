package com.example.simplerecord.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.simplerecord.util.RoomDatabaseBackupWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class BackupViewModel(application: Application) : AndroidViewModel(application) {

        fun startBackupProcess() {
            // 在 viewModelScope 中启动协程，确保在 ViewModel 清除时任务取消
            viewModelScope.launch {
                val context = getApplication<Application>().applicationContext

                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val inputData = Data.Builder()
                    .putString("DATABASE_NAME", "Data.db")
                    .putString("WEBDAV_URL", "https://ogi.teracloud.jp/dav")
                    .putString("WEBDAV_USERNAME", "zornet")
                    .putString("WEBDAV_PASSWORD", "Y4gDS672JqXbi6u9")
                    .putString("REMOTE_FOLDER_PATH", "AndroidRoomDBBackups")
                    .build()

                val backupWorkRequest = OneTimeWorkRequest.Builder(RoomDatabaseBackupWorker::class.java)
                    .setConstraints(constraints)
                    .setInputData(inputData)
                    .setBackoffCriteria(
                        BackoffPolicy.EXPONENTIAL,
                        WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                        TimeUnit.MILLISECONDS
                    )
                    .build()

                WorkManager.getInstance(context).enqueue(backupWorkRequest)

                // 如果你需要 UI 反馈，可以在这里更新 LiveData 或 StateFlow
                // backupStatus.postValue("Room 数据库备份任务已启动...")
                // Toast 应该在 UI 层显示，而不是 ViewModel
            }
        }
    }