package com.ddd.sonnypolabobe.global.config

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JasyptConfig {

    @Bean("jasyptStringEncryptor")
    fun stringEncryptor(): StringEncryptor = PooledPBEStringEncryptor().apply {
        this.setConfig(
            SimpleStringPBEConfig().apply {
                this.password = System.getenv("JASYPT_ENCRYPTOR_PASSWORD")
                this.algorithm = "PBEWithMD5AndDES"
                this.setKeyObtentionIterations("1000")
                this.setPoolSize("1")
                this.stringOutputType = "base64"
                this.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator")
                this.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator")
            }
        )
    }
}