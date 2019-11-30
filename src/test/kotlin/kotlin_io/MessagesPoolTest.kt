package kotlin_io

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK

import kotlin.test.*


class MessagesPoolTest {
    @MockK
    lateinit var dataChannel: SocketBasedDataChannel

    @MockK
    lateinit var dataChannelListener: SocketBasedDataChannelListener

    init {
        MockKAnnotations.init(this)
    }

    @Test fun `test `() {
        val messagesPool: MessagesPool = SocketBasedMessagesPool(dataChannel, dataChannelListener)
    }
}