
package kotlin_io


typealias Receiver = (message: Message) -> Unit

interface MessagesPool {
    fun send(ipAddress: String, port: Int, message: Message)
    fun listen(port: Int, receiver: Receiver)
    fun stop(port: Int)

    suspend fun listenAndFetch(port: Int): Message
}