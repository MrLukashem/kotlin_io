
package kotlin_io

import java.net.Socket
import java.io.OutputStream
import java.io.InputStream


open class SocketBasedDataChannel(socket: Socket,
    val onDataChannelClosed: () -> Unit = {}) : DataChannel {

    public val ipAddress: String
    public val port: Int

    private val ownSocket: Socket = socket
    private val outputStream: OutputStream
    private val inputStream: InputStream

    constructor(ipAddress: String, port: Int): this(Socket(ipAddress, port))

    // TODO: ipAddress should be special type, not a string.
    init {
        ipAddress = socket.getInetAddress().toString()
        port = socket.getPort()
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

    override fun read(): Byte? {
        val byteAsInt = inputStream.read()
        return if (byteAsInt != -1) byteAsInt.toByte() else null
    }

    override fun read(bytesArray: ByteArray): Int {
        return inputStream.read(bytesArray)
    }

    override fun isBound(): Boolean = ownSocket.isConnected
}
