package compiler

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

class InputStreamTest(){

    @Test
    fun testInputFile(){
        val stream = InputStream(ByteArrayInputStream("abc   \nd\nef".toByteArray()))
        assertEquals(stream.location.lineNumber,1)
        assertEquals(stream.location.columnNumber, 1)

        assertEquals('a', stream.readChar())
        assertEquals(stream.location.lineNumber, 1)
        assertEquals(stream.location.columnNumber, 2)

        stream.unreadChar('A')
        assertEquals(stream.location.lineNumber,1)
        assertEquals(stream.location.columnNumber , 1)

        assertEquals(stream.readChar(), 'A')
        assertEquals(stream.location.lineNumber,1)
        assertEquals(stream.location.columnNumber,2)

        assertEquals(stream.readChar(), 'b')
        assertEquals(stream.location.lineNumber, 1)
        assertEquals(stream.location.columnNumber, 3)

        assertEquals(stream.readChar(), 'c')
        assertEquals(stream.location.lineNumber, 1)
        assertEquals(stream.location.columnNumber ,4)

        stream.skipWhiteSpace()

        assertEquals(stream.readChar(), 'd')
        assertEquals(stream.location.lineNumber ,2)
        assertEquals(stream.location.columnNumber,2)

        assertEquals(stream.readChar(),'\n')
        assertEquals(stream.location.lineNumber, 3)
        assertEquals(stream.location.columnNumber,1)

        assertEquals(stream.readChar(), 'e')
        assertEquals(stream.location.lineNumber, 3)
        assertEquals(stream.location.columnNumber, 2)

        assertEquals(stream.readChar(),'f')
        assertEquals(stream.location.lineNumber,3)
        assertEquals(stream.location.columnNumber,3)

        //end of the file
        assertEquals('\u0000', stream.readChar())
    }

    @Test
    fun testEndOfFile(){
        val stream = InputStream(ByteArrayInputStream("a\u0000".toByteArray()))
        assertEquals('a', stream.readChar())
        assertEquals('\u0000', stream.readChar())
        assertEquals('\u0000', stream.readChar())
        stream.readChar()
        assertEquals('\u0000', stream.readChar())

    }
}