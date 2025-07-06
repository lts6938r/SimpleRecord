package com.example.simplerecord.util

import com.github.sardine.Sardine
import com.github.sardine.SardineFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URISyntaxException

class WebDAVUploader(
    private val webdavUrl: String,
    private val username: String,
    private val password: String
) {

    /**
     * 将给定文件上传到 WebDAV 服务器上指定的远程文件夹。
     *
     * @param fileToUpload 要上传的本地 File 对象。
     * @param remoteFolderPath WebDAV 服务器上的远程文件夹路径（例如，“AndroidBackups”）。
     * @return 如果上传成功则为 true，否则为 false。
     * @throws IOException 如果上传过程中发生 I/O 错误。
     * @throws URISyntaxException 如果 WebDAV URL 格式错误。
     */
    @Throws(IOException::class, URISyntaxException::class)
    fun uploadFile(fileToUpload: File, remoteFolderPath: String): Boolean {
        val sardine: Sardine = SardineFactory.begin(username, password)

        // 确保远程文件夹存在
        val remoteFolderUrl = "$webdavUrl/$remoteFolderPath"
        if (!sardine.exists(remoteFolderUrl)) {
            sardine.createDirectory(remoteFolderUrl)
        }

        // 构建文件的完整远程路径
        val remoteFilePath = "$remoteFolderUrl/${fileToUpload.name}"

        return try {
            FileInputStream(fileToUpload).use { inputStream ->
                sardine.put(remoteFilePath, inputStream)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}