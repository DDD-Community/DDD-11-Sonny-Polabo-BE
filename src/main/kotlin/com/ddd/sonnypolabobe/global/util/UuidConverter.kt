package com.ddd.sonnypolabobe.global.util

import java.nio.ByteBuffer
import java.util.*

object UuidConverter {
    fun byteArrayToUUID(byteArray: ByteArray): UUID {
        val byteBuffer = ByteBuffer.wrap(byteArray)
        val mostSigBits = byteBuffer.long
        val leastSigBits = byteBuffer.long
        return UUID(mostSigBits, leastSigBits)
    }

    fun uuidToByteArray(uuid: UUID): ByteArray {
        val byteBuffer = ByteBuffer.allocate(16)
        byteBuffer.putLong(uuid.mostSignificantBits)
        byteBuffer.putLong(uuid.leastSignificantBits)
        return byteBuffer.array()
    }

    fun stringToUUID(uuid: String): UUID {
        return UUID.fromString(uuid)
    }

    fun uuidToString(uuid: UUID): String {
        return uuid.toString()
    }
}