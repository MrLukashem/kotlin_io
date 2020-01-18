
package kotlin_io

import java.io.Closeable


interface DataChannel : Closeable {
    fun write(bytesArray: ByteArray)
    fun write(byte: Byte)

    fun read(): Byte?
    fun read(bytesArray: ByteArray): Int

    fun isBound(): Boolean
}

fun DataChannel.whileReadBytes(action: (byte: Byte) -> Unit) {
    var byte = read()
    while (byte != null) {
        action(byte)
        byte = read()
    }
}
