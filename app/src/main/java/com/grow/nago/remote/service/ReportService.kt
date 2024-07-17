package com.grow.nago.remote.service

import com.grow.nago.remote.request.ReportGetRequest
import com.grow.nago.remote.request.ReportRequest
import com.grow.nago.remote.response.BaseResponse
import com.grow.nago.remote.response.ReportResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("/report/get-all")
    suspend fun getAllReport(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("phone") phone: String,
    ): BaseResponse<List<ReportResponse>>

    @GET("/report/get/{id}")
    suspend fun getReport(
        @Path("id") id: Int,
    ): BaseResponse<ReportResponse>

    @DELETE("/report/del/{id}")
    suspend fun deleteReport(
        @Path("id") id: Int,
    ): BaseResponse<Unit>
}