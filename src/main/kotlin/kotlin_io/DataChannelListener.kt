
package kotlin_io

import kotlin_io.DataChannel


interface DataChannelListener {
    fun listen(onDataChannelBound: (dataChannel: DataChannel) -> Unit): Unit
    fun detach(): Unit

    suspend fun listenAndFetch(): DataChannel
}