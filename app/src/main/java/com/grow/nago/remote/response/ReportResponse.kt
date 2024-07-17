package com.grow.nago.remote.response

data class ReportResponse(
    val id: Int,
    val firstImage: String,
    val secondImage: String?,
    val title: String,
    val content: String,
    val large: String,
    val small: String,
    val name: String?,
    val email: String?,
    val phone: String?,
    val lat: String?,
    val lng: String?,
    val address: String?,
    val createdAt: List<Int>,
)
