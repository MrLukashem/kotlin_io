
package kotlin_io

import kotlin_io.DataChannel

interface DataChannelListener {
    fun listen(onDataChannelBound: (dataChannel: DataChannel) -> Unit): Int
    fun detach(listenerFunId: Int): Unit
}