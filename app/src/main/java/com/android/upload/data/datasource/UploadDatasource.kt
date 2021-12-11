package com.android.upload.data.datasource

import com.android.upload.domain.Result
import com.android.upload.domain.entity.Response
import okhttp3.MultipartBody

interface UploadDatasource {
    suspend fun uploadImage(body: MultipartBody.Part): Result<Response>
}