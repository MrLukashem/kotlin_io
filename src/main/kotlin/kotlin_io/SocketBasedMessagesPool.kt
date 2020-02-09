
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

    // TODO: specific class shoud be used to keep ip address - not string
    override fun send(ipAddress: String, port: Int, message: Message) {
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
            receiver(createMessageFromDataChannel(it))
        }

        portToListener[port] = listener
    }

    override suspend fun listenAndFetch(port: Int): Message {
        val listener = channelListenerFactory.invoke(port)
        val channel = listener.listenAndFetch()

        return createMessageFromDataChannel(channel)
    }

    fun createMessageFromDataChannel(channel: DataChannel): Message {
        val bytesList = mutableListOf<Byte>()
        var currentByte: Byte = 0

        channel.whileReadBytes {
            bytesList.add(currentByte)
        }

        return Message.fromRawData(bytesList.toByteArray())
    }

    override fun stop(port: Int) {
        portToListener[port]?.detach()
        portToListener.remove(port)
    }
}