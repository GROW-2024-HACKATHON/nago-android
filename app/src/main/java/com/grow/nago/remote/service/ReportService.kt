package com.grow.nago.remote.service

import com.grow.nago.remote.response.BaseResponse
import com.grow.nago.remote.response.ReportResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ReportService {

    @Multipart
    @POST("/report/upload")
    suspend fun reportUpload(
        @Part image: MultipartBody.Part
    ): BaseResponse<ReportResponse>
}