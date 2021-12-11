package com.android.upload.domain.repo


import com.android.upload.domain.Result
import com.android.upload.domain.entity.Response
import okhttp3.MultipartBody

interface Repository {
    suspend fun uploadImage(body: MultipartBody.Part): Result<Response>
}