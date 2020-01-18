package kotlin_io

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifyAll
import io.mockk.every
import io.mockk.mockk

import kotlin.test.*


class MessagesPoolTest {
    @MockK
    lateinit var dataChannelFactory: DataChannelFactory

    @MockK
    lateinit var dataChannelListenerFactory: DataChannelListenerFactory

    init {
        MockKAnnotations.init(this)
    }

    @Test fun `test properly construction of MessagesPool`() {
        verify(exactly = 0) {
            dataChannelFactory.invoke(any(), any())
            dataChannelListenerFactory(any())
        }

        SocketBasedMessagesPool(dataChannelFactory, dataChannelListenerFactory)
    }

    @Test fun `test send with empty message`() {
        val message = Message.create(2, byteArrayOf())
        val messagesPool: MessagesPool = SocketBasedMessagesPool(
            dataChannelFactory,
            dataChannelListenerFactory)
        val dataChannel = mockk<DataChannel>(relaxed = true)

        every {
            dataChannelFactory.invoke(any(), any())
        } returns dataChannel

        messagesPool.send("192.168.255.255", 4500, message)

        verify {
            dataChannelFactory.invoke("192.168.255.255", 4500)
        }
        verify {
            dataChannel.write(any<ByteArray>())
        }
    }
}