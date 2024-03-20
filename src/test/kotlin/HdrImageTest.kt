import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteOrder

class HdrImageTest {

    var SampleImage = HdrImage(10,10)

    @Test
    fun getPixel(){
        assert(2 in (0 until 10)) { "Error: x value not valid" }
        assert(3 in (0 until 10)) { "Error: y value not valid" }
    }
    @Test
    fun setPixel(){
        assert(5 in (0 until 10)) { "Error: x value not valid" }
        assert(4 in (0 until 10)) { "Error: y value not valid" }
    }

    @Test
    fun writePFM(){
         var img = HdrImage(3, 2)

        img.setPixel(0, 0, Color(1.0F, 2.0F, 3.0F)) 
        img.setPixel(1, 0, Color(4.0F, 5.0F, 6.0F)) 
        img.setPixel(2, 0, Color(7.0F, 8.0F, 9.0F)) 
        img.setPixel(0, 1, Color(100F, 200F, 300F)) 
        img.setPixel(1, 1, Color(400F, 500F, 600F))
        img.setPixel(2, 1, Color(700F, 800F, 900F))

        println("ok")
        val out = FileOutputStream("output.txt");
        img.writePFM(out, ByteOrder.LITTLE_ENDIAN)

        //da terminare

    }

}
