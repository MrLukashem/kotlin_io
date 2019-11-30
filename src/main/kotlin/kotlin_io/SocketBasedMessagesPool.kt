package kotlin_io


class SocketBasedMessagesPool(
        socketBasedDataChannel: SocketBasedDataChannel,
        socketBasedDataChannelListener: SocketBasedDataChannelListener)
    : MessagesPool {
    override fun send(message: Message) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listen(myPort: Int, senderPort: Int, receiver: (message: Message) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}