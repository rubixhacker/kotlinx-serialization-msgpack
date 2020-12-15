package com.ensarsarajcic.kotlinx.serialization.msgpack

import kotlinx.serialization.modules.SerializersModule
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MsgPackDecoderTest {
    @Test
    fun testTrueDecode() {
        val decoder = MsgPackDecoder(MsgPackConfiguration.default, SerializersModule {}, byteArrayOf(0xc3.toByte()))
        assertEquals(true, decoder.decodeBoolean())
    }

    @Test
    fun testFalseDecode() {
        val decoder = MsgPackDecoder(MsgPackConfiguration.default, SerializersModule {}, byteArrayOf(0xc2.toByte()))
        assertEquals(false, decoder.decodeBoolean())
    }

    @Test
    fun testNullDecode() {
        val decoder = MsgPackDecoder(MsgPackConfiguration.default, SerializersModule {}, byteArrayOf(0xc0.toByte()))
        assertEquals(null, decoder.decodeNull())
    }

    @Test
    fun testByteDecode() {
        fun testByteDecoding(input: ByteArray, expectedResult: Byte) = MsgPackDecoder(MsgPackConfiguration.default, SerializersModule {}, input).also {
            println("${input.toList()}, $expectedResult")
            assertEquals(expectedResult, it.decodeByte())
        }
        fun testByteDecoding(input: Byte, expectedResult: Byte) = testByteDecoding(byteArrayOf(input), expectedResult)

        testByteDecoding(0x37, 55)
        testByteDecoding(0xe0.toByte(), -32)
        testByteDecoding(0x7f.toByte(), 127)
        testByteDecoding(0x00, 0)
        testByteDecoding(0xff.toByte(), -1)
        testByteDecoding(0xfe.toByte(), -2)
        testByteDecoding(byteArrayOf(0xd0.toByte(), 0xdf.toByte()), -33)
        testByteDecoding(byteArrayOf(0xd0.toByte(), 0xce.toByte()), -50)
        testByteDecoding(byteArrayOf(0xd0.toByte(), 0x81.toByte()), -127)
    }
}
