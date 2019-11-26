
package kotlin_io

import kotlin_io.DataChannelListener
import kotlin_io.SocketBasedDataChannel

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

import java.net.ServerSocket

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SocketBasedDataChannelListener(port: Int) : DataChannelListener, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    val ownServerSocket: ServerSocket
    var listenerId: Int = Int.MAX_VALUE
    val listenerToJob: HashMap<Int, Job> = hashMapOf()
 
    private var job: Job = Job()
    private val logger: Logger = LoggerFactory.getLogger("SocketBasedDataChannelListener")

    init {
        ownServerSocket = ServerSocket(port)
        println(job)
    }

    override fun listen(onDataChannelBound: (dataChannel: DataChannel) -> Unit): Int {
        val job = launch(coroutineContext) {
            while(true) {
                logger.info(" started in thread ${Thread.currentThread().name}")

                val channelSocket = ownServerSocket.accept()
                val dataChannel: DataChannel = SocketBasedDataChannel(channelSocket)

                onDataChannelBound(dataChannel)
            }
        }

        listenerId = onDataChannelBound.hashCode()
        listenerToJob[listenerId] = job
        return listenerId
    }

    override fun detach(listenerFunId: Int) {
        if (listenerId != listenerFunId) {
            logger.info(" cannot detach listener id = ${listenerFunId}")
            return
        }

        listenerToJob[listenerId]?.cancel()
    }
}