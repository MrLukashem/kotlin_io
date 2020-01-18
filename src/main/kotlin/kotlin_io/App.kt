/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package kotlin_io

import kotlin_io.SocketBasedDataChannel
import kotlin_io.SocketBasedDataChannelListener

import kotlin.concurrent.*
import kotlin.io.*

import java.io.IOException
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.Date


class App {
    val greeting: String
        get() {
            return "Hello world."
        }
}

fun main(args: Array<String>) {
    val listener2 = SocketBasedDataChannelListener(5003)
    listener2.listen {
        val i = it.read()
        println("socket attached with code = ${i}")
    }
    Thread.sleep(5000)
    

    val dataChannel: DataChannel = SocketBasedDataChannel("0.0.0.0", 5003)
    dataChannel.use {
        it.write(88)
    }

    listener2.detach()
    listener2.listen {
        val i = it.read()
        println("socket attached with code = ${i}")
    }
    Thread.sleep(5000)
}
