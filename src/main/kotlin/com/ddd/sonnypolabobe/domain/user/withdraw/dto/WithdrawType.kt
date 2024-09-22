package com.ddd.sonnypolabobe.domain.user.withdraw.dto

enum class WithdrawType(val description : String) {
    NOT_USE("더이상 사용하지 않아요"),
    WORRY_ABOUT_PERSONAL_INFO("개인정보 우려"),
    DROP_MY_DATA("내 데이터 삭제"),
    WANT_TO_NEW_ACCOUNT("새로운 계정을 만들고 싶어요"),
    OTHER("기타")
}