
import java.io.Serializable
import java.nio.ByteBuffer
import java.nio.BufferOverflowException

data class Message(
    public val msgId: Int,
    public val msgType: Int,
    public val senderPort: Int,
    public val receiverPort: Int,
    public val payload: ByteArray
) : Serializable {
    class MalformedDataException(message: String?) : RuntimeException(message)

    companion object {
        fun createFromRawData(data: ByteArray): Message {
            var buffer = ByteBuffer.wrap(data)

            return try {
                val msgId = buffer.getInt()
                val msgType = buffer.getInt()
                val senderPort = buffer.getInt()
                val receiverPort = buffer.getInt()
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
