package com.grow.nago.remote.request

data class ReportRequest(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val lat: String,
    val lng: String,
    val address: String,
)