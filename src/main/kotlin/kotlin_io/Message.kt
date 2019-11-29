
package kotlin_io

import java.io.Serializable
import java.nio.ByteBuffer
import java.nio.BufferOverflowException


data class Message(
    val msgId: Int,
    val msgType: Int,
    val senderPort: Int,
    val receiverPort: Int,
    val payload: ByteArray
) : Serializable {
    class MalformedDataException(message: String?) : RuntimeException(message)

    companion object {
        fun createFromRawData(data: ByteArray): Message {
            val buffer = ByteBuffer.wrap(data)

            return try {
                val msgId = buffer.int
                val msgType = buffer.int
                val senderPort = buffer.int
                val receiverPort = buffer.int
                val payload = buffer.slice().array()

                Message(msgId, msgType, senderPort, receiverPort, payload)
            } catch(overflowExc: BufferOverflowException) {
                throw MalformedDataException(overflowExc.message)
            } catch(exc: Exception) {
                throw MalformedDataException("A data is malformed. Please check byte sequence.")
            }
        }
    }
}
