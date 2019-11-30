
package kotlin_io


interface MessagesPool {
    fun send(message: Message)
    fun listen(myPort: Int, senderPort: Int, receiver: (message: Message) -> Unit)
}