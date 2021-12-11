package com.android.upload.domain.usecase

import com.android.upload.domain.Result
import com.android.upload.domain.entity.Response
import com.android.upload.domain.repo.Repository
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(body: MultipartBody.Part): Result<Response> = repository.uploadImage(body)
}
