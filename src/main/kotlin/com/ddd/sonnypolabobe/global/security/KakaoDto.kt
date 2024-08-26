package com.ddd.sonnypolabobe.global.security

import java.util.*

class KakaoDto {

    companion object {
        data class Token(
            val access_token: String,
            val refresh_token: String,
            val token_type: String,
            val expires_in: Int,
            val refresh_token_expires_in: Int,
            val scope: String
        )

        data class UserInfo(
            val id: Long,
            val connected_at: String,
            val properties: Map<String, String>,
            val kakao_account: KakaoAccount
        )

        data class KakaoAccount(
            val profile_nickname_needs_agreement: Boolean,
            val profile_image_needs_agreement: Boolean,
            val profile: Profile,
            val has_email: Boolean,
            val email_needs_agreement: Boolean,
            val is_email_valid: Boolean,
            val is_email_verified: Boolean,
            val email: String
        )

        data class Profile(
            val nickname: String,
            val thumbnail_image_url: String,
            val profile_image_url: String,
            val is_default_image: Boolean,
            val is_default_nickname: Boolean
        )
    }
}