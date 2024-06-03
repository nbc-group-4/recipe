package nbc.group.recipes.data.repository

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import nbc.group.recipes.data.network.FirebaseResult
import java.io.InputStream
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage
): StorageRepository {
    override suspend fun putImage(
        storagePath: String,
        inputStream: InputStream
    ): FirebaseResult<Boolean> {
        return try {
            val profileRef = firebaseStorage.reference
                .child(storagePath)
            val result = profileRef.putStream(inputStream).await()
            FirebaseResult.Success(true)
        } catch(e: Exception) {
            FirebaseResult.Failure(e)
        }
    }
}
