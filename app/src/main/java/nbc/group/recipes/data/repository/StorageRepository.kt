package nbc.group.recipes.data.repository

import android.net.Uri
import nbc.group.recipes.data.network.FirebaseResult
import java.io.FileInputStream
import java.io.InputStream

interface StorageRepository {
    suspend fun putImage(
        storagePath: String,
        inputStream: InputStream
    ): FirebaseResult<Boolean>
}