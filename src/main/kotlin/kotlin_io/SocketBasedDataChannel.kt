
package kotlin_io

import java.net.Socket
import java.net.InetAddress
import java.io.OutputStream
import java.io.InputStream

import org.slf4j.Logger
import org.slf4j.LoggerFactory


open class SocketBasedDataChannel(val ownSocket: Socket) : DataChannel {
    private val outputStream: OutputStream
    private val inputStream: InputStream

    protected val logger: Logger = LoggerFactory.getLogger("SocketBasedDataChannel")

    constructor(ipAddress: String, port: Int) : this(Socket(InetAddress.getByName(ipAddress), port))
    // TODO: ipAddress should be special type, not a string.
    init {
        logger.info("init")
        outputStream = ownSocket.outputStream
        inputStream = ownSocket.inputStream
    }

    override fun close() {
        inputStream.close()
        outputStream.close()
        ownSocket.close()
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
