package com.grow.nago.remote.service

import com.grow.nago.remote.request.ReportRequest
import com.grow.nago.remote.response.BaseResponse
import com.grow.nago.remote.response.ReportResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface ReportService {

    @Multipart
    @POST("/report/upload")
    suspend fun reportUpload(
        @Part image: MultipartBody.Part
    ): BaseResponse<ReportResponse>

    @Multipart
    @POST("/report/add-image")
    suspend fun addImageUpload(
        @Part image: MultipartBody.Part,
        @Part("id") id: RequestBody
    ): BaseResponse<ReportResponse>

    @PATCH("/report/finish")
    suspend fun reportFinish(
        @Body body: ReportRequest
    ): BaseResponse<ReportResponse>
}