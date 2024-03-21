import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.nio.ByteOrder

class Read_Hdr_functionsKtTest{

    @Test
    fun readFloat() {}

    @Test
    fun readLine(){
        val byteArray=ByteArrayInputStream("Hello\nworld".toByteArray())
        assert(readline(byteArray)=="Hello")
        assert(readline(byteArray)=="world")
    }
    @Test
    fun parseImageSize(){
        val sampleImageSize=parseImageSize("2 3")
        assertEquals(2,sampleImageSize[0])
        assertEquals(3,sampleImageSize[1])
    }
    @Test
    fun parseEndianness(){
        assertEquals(ByteOrder.BIG_ENDIAN,parseEndianness("1.0")) {"not Big Endian value"}
        assertEquals(ByteOrder.LITTLE_ENDIAN, parseEndianness("-1.0")) {"not Small Endian value"}
    }

    @Test
    fun readPfmImage(){

    }
}