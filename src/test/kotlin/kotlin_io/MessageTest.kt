/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package kotlin_io

import kotlin_io.Message
import java.util.*
import kotlin.test.*


class MessageTest {
    @Test fun `test simple message values`() {
        val message = Message(99, 2, 2000, 3400, byteArrayOf())
        assertEquals(message.msgId, 99)
        assertEquals(message.msgType, 2)
        assertEquals(message.senderPort, 2000)
        assertEquals(message.receiverPort, 3400)
        assertTrue(message.payload.isEmpty())
    }

    @Test fun `test message with custom byte array`() {
        val message = Message(111, 32, 2042, 3434,
                byteArrayOf(0xFF.toByte(), 0x32.toByte(), 0x00.toByte()))
        assertEquals(message.msgId, 111)
        assertEquals(message.msgType, 32)
        assertEquals(message.senderPort, 2042)
        assertEquals(message.receiverPort, 3434)
        assertTrue(message.payload.contentEquals(byteArrayOf(0xFF.toByte(), 0x32.toByte(), 0x00.toByte())))
    }

    @Test fun `test message creating using factory method`() {
        val inputArray = byteArrayOf(
                0x00, 0x00, 0x00, 0x05,
                0x00, 0x00, 0x00, 0x80.toByte(),
                0x00, 0x00, 0x00, 0x08,
                0x00, 0x00, 0x32, 0x00,
                0x01, 0x02, 0x32, 0x32)
        val message = Message.createFromRawData(inputArray)

        assertEquals(message.msgId, 5)
        assertEquals(message.msgType, 128)
        assertEquals(message.senderPort, 8)
        assertEquals(message.receiverPort, 12800)
        assertTrue(message.payload.contentEquals(byteArrayOf(0x01, 0x02, 0x32, 0x32)))
    }
}