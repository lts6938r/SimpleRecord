package com.example.simplerecord.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.io.File
import java.io.IOException
import java.net.URISyntaxException

class RoomDatabaseBackupWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // 从输入数据中检索参数
        val dbName = inputData.getString("DATABASE_NAME")
        val webdavUrl = inputData.getString("WEBDAV_URL")
        val webdavUsername = inputData.getString("WEBDAV_USERNAME")
        val webdavPassword = inputData.getString("WEBDAV_PASSWORD")
        val remoteFolderPath = inputData.getString("REMOTE_FOLDER_PATH")

        // 验证所有必需的输入数据都存在
        if (dbName == null || webdavUrl == null ||
            webdavUsername == null || webdavPassword == null || remoteFolderPath == null) {
            return Result.failure() // 缺少必要参数，任务失败
        }

        var backupFile: File? = null
        return try {
            // 1. 将 Room 数据库文件复制到临时位置
            val backupHelper = DatabaseFileBackupHelper(applicationContext, dbName)
            backupFile = backupHelper.copyDatabaseFileToTemporaryLocation()

            if (backupFile.exists()) {
                // 2. 上传到 WebDAV
                val uploader = WebDAVUploader(webdavUrl, webdavUsername, webdavPassword)
                val success = uploader.uploadFile(backupFile, remoteFolderPath)

                if (success) {
                    Result.success() // 备份成功
                } else {
                    Result.failure() // 上传失败
                }
            } else {
                Result.failure() // 复制文件失败
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Result.retry() // I/O 错误，可以重试
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            Result.failure() // URL 格式错误，通常不应重试
        } catch (e: Exception) { // 捕获其他所有意外异常
            e.printStackTrace()
            Result.failure()
        } finally {
            // 清理本地备份文件（无论成功与否，都删除临时文件）
            backupFile?.delete()
        }
    }
}

