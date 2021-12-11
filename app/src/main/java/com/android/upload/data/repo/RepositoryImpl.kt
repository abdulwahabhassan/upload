package com.android.upload.data.repo

import com.android.upload.data.datasource.UploadDatasource
import com.android.upload.domain.Result
import com.android.upload.domain.repo.Repository
import com.android.upload.domain.entity.Response
import okhttp3.MultipartBody
import javax.inject.Inject

class RepositoryImpl @Inject constructor  (
    private val remoteDatasource: UploadDatasource
) : Repository {
    override suspend fun uploadImage(body: MultipartBody.Part): Result<Response> {
        return remoteDatasource.uploadImage(body)
    }

}