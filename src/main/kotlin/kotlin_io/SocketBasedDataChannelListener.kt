
package kotlin_io

import kotlin_io.DataChannelListener
import kotlin_io.SocketBasedDataChannel

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

import java.net.ServerSocket

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class SocketBasedDataChannelListener(val port: Int) : DataChannelListener {
    class ListenerNotDetachedException : RuntimeException("Old listener has not been detached")

    var worker : Job? = null
 
    protected val logger: Logger = LoggerFactory.getLogger("SocketBasedDataChannelListener")
    protected val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun listen(onDataChannelBound: (dataChannel: DataChannel) -> Unit) {
        if (worker != null) {
            throw ListenerNotDetachedException()
        }

        val job = scope.launch {
            val ownServerSocket = ServerSocket(port)

            while (true) {
                logger.info("Started in thread ${Thread.currentThread().name}")
                logger.info("Waiting for the server connection acceptance")

                val channelSocket = ownServerSocket.accept()
                val dataChannel: DataChannel = SocketBasedDataChannel(channelSocket)

                logger.info("DataChannel created. Calling dataChannelCallbck")
                onDataChannelBound(dataChannel)
            }
        }

        worker = job
    }

    override suspend fun listenAndFetch(): DataChannel {
        val ownServerSocket = ServerSocket(port)
        logger.info("Waiting for the server connection acceptance")

        return SocketBasedDataChannel(ownServerSocket.accept())
    }

    override fun detach() {
        worker?.cancel()
        worker = null
    }
}