package com.grow.nago.remote.response

data class BaseResponse<T> (
    val success: Boolean,
    val message: String,
    val data: T
)