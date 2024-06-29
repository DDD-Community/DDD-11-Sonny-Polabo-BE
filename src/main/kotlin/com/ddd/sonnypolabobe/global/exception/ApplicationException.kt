package com.ddd.sonnypolabobe.global.exception

data class ApplicationException(
    val error : CustomErrorCode
) :RuntimeException(error.message)