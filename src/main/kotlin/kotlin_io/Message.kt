
package kotlin_io

import java.nio.ByteBuffer
import java.nio.BufferOverflowException


data class Message(
    val msgType: Int,
    val answerTo: Int,
    val payload: ByteArray) {
    class MalformedDataException(message: String?) : RuntimeException(message)

    val msgId: Int
        get() = this.hashCode()

    companion object {
        fun create(msgType: Int, payload: ByteArray, answerTo: Int = Int.MIN_VALUE)
            = Message(msgType, answerTo, payload)

        fun fromRawData(data: ByteArray): Message {
            val buffer = ByteBuffer.wrap(data)

            return try {
                val msgType = buffer.int
                val answerTo = buffer.int
                val payload = ByteArray(buffer.remaining())
                buffer.get(payload)

                Message(msgType, answerTo, payload)
            } catch(overflowExc: BufferOverflowException) {
                throw MalformedDataException(overflowExc.message)
            } catch(exc: Exception) {
                throw MalformedDataException("A data is malformed. Please check byte sequence.")
            }
        }

        fun toRawData(message: Message): ByteArray {
            val descMembersSize = 8
            return ByteBuffer.allocate(descMembersSize + message.payload.size).apply {
                putInt(message.msgId)
                putInt(message.answerTo)
                put(message.payload)
            }.array()
        }
    }
}

fun Message.makeAnswer(msgType: Int, payload: ByteArray): Message
    = Message.create(msgType, payload, this.msgId)
