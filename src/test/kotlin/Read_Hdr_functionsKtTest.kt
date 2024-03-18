import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.nio.ByteOrder

class Read_Hdr_functionsKtTest{
    @Test
    fun testParseImageSize(){
        val SampleImageSize=parseImageSize("2 3")
        assertEquals(2,SampleImageSize[0])
        assertEquals(3,SampleImageSize[1])
    }
    @Test
    fun testParseEndianness(){
        assertEquals(ByteOrder.BIG_ENDIAN,parseEndianness("1.0")) {"not Big Endian value"}
        assertEquals(ByteOrder.LITTLE_ENDIAN, parseEndianness("-1.0")) {"not Small Endian value"}
    }
    @Test
    fun testReadLine(){
    val byteArray=ByteArrayInputStream("Hello\nworld".toByteArray())
    assert(readline(byteArray)=="Hello")
    assert(readline(byteArray)=="world")
    }
}