/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package kotlin_io

import kotlin.test.Test
import kotlin.test.assertNotNull

import java.io.ObjectOutputStream
import java.io.ObjectInputStream 
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream
import java.io.Serializable

data class Foo(public val data: Int) : Serializable {
}

class AppTest {
    @Test fun testAppHasAGreeting() {
        val classUnderTest = App()
        assertNotNull(classUnderTest.greeting, "app should have a greeting")
    }

    @Test fun testSocketBasedDataChannel() {
        var byteArrayS = ByteArrayOutputStream();
        var objectOS = ObjectOutputStream(byteArrayS)

        var foo = Foo(9991)

        objectOS.writeObject(foo)

        var inputByteArray = ByteArrayInputStream(byteArrayS.toByteArray())
        var input = ObjectInputStream(inputByteArray)

        fun readAny(iss: ObjectInputStream): Any {
            return iss.readObject()
        }
        var foo2 = readAny(input)
        if (foo2 is Foo) {
            print("isFOOO = ${foo2.data}")
        }
        else
        {
            print("NO FOO")
        }


    }
}
