package com.android.upload.data.mapper

import com.android.upload.data.model.ApiResponse
import com.android.upload.domain.entity.Response

class ResponseMapper {
    fun mapToEntity(type: ApiResponse): Response {
        return Response(
            type.timestamp,
            type.status,
            type.error,
            type.message,
            type.path
        )
    }
}