
package kotlin_io

import kotlin_io.DataChannelListener
import kotlin_io.SocketBasedDataChannel

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

import java.net.ServerSocket

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class SocketBasedDataChannelListener(port: Int) : DataChannelListener {
    class ListenerNotDetachedException : RuntimeException("Old listener has not been detached")

    val ownServerSocket: ServerSocket
    var listenerId: Int = Int.MAX_VALUE
    var worker : Job? = null
 
    protected val logger: Logger = LoggerFactory.getLogger("SocketBasedDataChannelListener")
    protected val scope = CoroutineScope(Job() + Dispatchers.IO)

    init {
        ownServerSocket = ServerSocket(port)
    }

    override fun listen(onDataChannelBound: (dataChannel: DataChannel) -> Unit) {
        if (worker != null) {
            throw ListenerNotDetachedException()
        }

        val job = scope.launch {
            while(true) {
                logger.info(" started in thread ${Thread.currentThread().name}")

                val channelSocket = ownServerSocket.accept()
                val dataChannel: DataChannel = SocketBasedDataChannel(channelSocket)

                onDataChannelBound(dataChannel)
            }
        }

        worker = job
    }

    override fun detach() {
        worker?.cancel()
        worker = null
    }
}