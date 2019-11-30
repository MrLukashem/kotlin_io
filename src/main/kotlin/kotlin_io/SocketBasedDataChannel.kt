
package kotlin_io

import java.net.Socket
import java.io.OutputStream
import java.io.InputStream

open class SocketBasedDataChannel(socket: Socket,
    val onDataChannelClosed: () -> Unit = {}) : DataChannel {

    private val ownSocket: Socket = socket
    private val outputStream: OutputStream
    private val inputStream: InputStream

    constructor(ipAddress: String, port: Int): this(Socket(ipAddress, port))

    init {
        outputStream = ownSocket.outputStream
        inputStream = ownSocket.inputStream
    }

    override fun close() {
        inputStream.close()
        outputStream.close()
        ownSocket.close()

        onDataChannelClosed.invoke()
    }

    override fun write(bytesArray: ByteArray) {
        outputStream.write(bytesArray)
    }

    override fun write(byte: Byte) {
        outputStream.write(byte.toInt())
    }

    override fun read(): Byte {
        val byteAsInt = inputStream.read()
        return byteAsInt.toByte()
    }

    override fun read(bytesArray: ByteArray) {
        inputStream.read(bytesArray)
    }

    override fun isBound(): Boolean = ownSocket.isConnected
}
