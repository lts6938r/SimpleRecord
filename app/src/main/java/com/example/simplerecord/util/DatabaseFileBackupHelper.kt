package com.example.simplerecord.util

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

class DatabaseFileBackupHelper(
    private val context: Context,
    private val databaseName: String // e.g., "your_database_name.db"
) {

    /**
     * Finds the app's SQLite database file and copies it to a temporary location
     * within the app's external files directory.
     *
     * @return The File object representing the copied database file.
     * @throws IOException if the original database file is not found or copying fails.
     */
    @Throws(IOException::class)
    fun copyDatabaseFileToTemporaryLocation(): File {
        // Get the original database file path
        val originalDbFile = context.getDatabasePath(databaseName)

        if (!originalDbFile.exists()) {
            throw IOException("Original database file not found: ${originalDbFile.absolutePath}")
        }

        // Create a timestamped name for the backup file
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val backupFileName = databaseName.replace(".db", "") + "_full_backup_" + timeStamp + ".db"

        // Get the temporary output directory (e.g., app's external files directory)
        val tempDir = context.getExternalFilesDir(null)
            ?: throw IOException("Could not access external files directory for temporary backup.") // Or context.cacheDir for more temporary files

        if (!tempDir.exists() && !tempDir.mkdirs()) {
            throw IOException("Failed to create temporary directory: ${tempDir.absolutePath}")
        }

        val tempBackupFile = File(tempDir, backupFileName)

        var sourceChannel: FileChannel? = null
        var destinationChannel: FileChannel? = null

        try {
            sourceChannel = FileInputStream(originalDbFile).channel
            destinationChannel = FileOutputStream(tempBackupFile).channel
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size())
            return tempBackupFile
        } finally {
            sourceChannel?.close()
            destinationChannel?.close()
        }
    }
}