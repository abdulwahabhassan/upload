package com.android.upload.data.datasource

import android.util.Log
import com.android.upload.data.api.DarotApi
import com.android.upload.data.mapper.ResponseMapper
import com.android.upload.domain.Result
import com.android.upload.domain.entity.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadDatasourceImpl @Inject constructor(
    private val service: DarotApi,
    private val dispatcherIO: CoroutineDispatcher,
    private val responseMapper: ResponseMapper
        ) : UploadDatasource {
    override suspend fun uploadImage(body: MultipartBody.Part): Result<Response> = withContext(dispatcherIO) {
        try {
            val response = service.uploadImage(body)
            if (response.isSuccessful) {
                Log.d(TAG, "${response.body()!!}")
                return@withContext com.android.upload.domain.Result.Success(
                    responseMapper.mapToEntity(response.body()!!)
                )
            } else {
                Log.d(TAG, "${response.body()!!}")
                return@withContext com.android.upload.domain.Result.Error(
                    Exception(response.message())
                )
            }
        }catch (e: Exception) {
            Log.d(TAG, "${e.message}")
            return@withContext com.android.upload.domain.Result.Error(e)
        }
    }

    companion object {
        const val TAG = "Datasource"
    }

}