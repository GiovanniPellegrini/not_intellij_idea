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

        // not finished we need to develop function skipWhiteSpace
    }
}