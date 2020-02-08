
package kotlin_io

import java.net.InetAddress
import java.net.ConnectException

import org.slf4j.Logger
import org.slf4j.LoggerFactory


typealias DataChannelFactory = (ipAddress: String, port: Int) -> DataChannel
typealias DataChannelListenerFactory = (port: Int) -> DataChannelListener

class SocketBasedMessagesPool(
    val channelFactory: DataChannelFactory,
    val channelListenerFactory: DataChannelListenerFactory)
    : MessagesPool {
    class ConnectionRefused : RuntimeException("Connection has been refused")

    private val portToListener: MutableMap<Int, DataChannelListener> = hashMapOf()

    protected val logger: Logger = LoggerFactory.getLogger("SocketBasedDataChannel")

    override fun send(ipAddress: String, port: Int, message: Message) {
        // TODO: Test when connection is refused
        try {
            channelFactory.invoke(ipAddress, port).use {
                it.write(Message.toRawData(message))
            }
        } catch(ConnExc: ConnectException) {
            logger.info("Connection has been refused. Please check connection data")
            throw ConnectionRefused()
        }
    }

    override fun listen(port: Int, receiver: Receiver) {
        val listener = channelListenerFactory.invoke(port)
        listener.listen {
            val channel = it
            val bytesList = mutableListOf<Byte>()
            var currentByte: Byte = 0

            channel.whileReadBytes {
                bytesList.add(currentByte)
            }

            receiver(Message.fromRawData(bytesList.toByteArray()))
        }

        portToListener[port] = listener
    }

    override fun stop(port: Int) {
        portToListener[port]?.detach()
        portToListener.remove(port)
    }
}