
package kotlin_io


typealias DataChannelFactory = (ipAddress: String, port: Int) -> DataChannel
typealias DataChannelListenerFactory = (port: Int) -> DataChannelListener

class SocketBasedMessagesPool(
    val channelFactory: DataChannelFactory,
    val channelListenerFactory: DataChannelListenerFactory)
    : MessagesPool {

    val portToListener: MutableMap<Int, DataChannelListener> = hashMapOf()

    override fun send(ipAddress: String, port: Int, message: Message) {
        channelFactory.invoke(ipAddress, port).use {
            it.write(Message.toRawData(message))
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