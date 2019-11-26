
package kotlin_io

import java.io.Closeable

interface DataChannel : Closeable {
    fun write(bytesArray: ByteArray)
    fun write(byte: Byte)

    fun read(): Byte
    fun read(bytesArray: ByteArray)

    fun isBound(): Boolean
}