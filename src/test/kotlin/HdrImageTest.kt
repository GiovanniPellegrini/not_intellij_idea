import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.nio.ByteOrder

/**
 * initialize array from hexadecimal values
 */
fun byteArrayOfInts(vararg ints: Int) =
    ByteArray(ints.size) { pos -> ints[pos].toByte() }

class HdrImageTest {

    @Test
    fun getSetPixelTest(){
        val img = HdrImage(7,4)
        val referenceCol = Color(1f,2f,3f)
        img.setPixel(3,2,referenceCol)
        assert(referenceCol.areClose(img.getPixel(3,2)))
    }

    @Test
    fun getPixel() {
        assert(2 in (0 until 10)) { "Error: x value not valid" }
        assert(3 in (0 until 10)) { "Error: y value not valid" }
    }

    @Test
    fun setPixel() {
        assert(5 in (0 until 10)) { "Error: x value not valid" }
        assert(4 in (0 until 10)) { "Error: y value not valid" }
    }

    @Test
    fun writePFM() {
        val img = HdrImage(3, 2)
        val streamBe = ByteArrayOutputStream()
        val streamLe = ByteArrayOutputStream()

        img.setPixel(0, 0, Color(1.0e1F, 2.0e1F, 3.0e1F))
        img.setPixel(1, 0, Color(4.0e1F, 5.0e1F, 6.0e1F))
        img.setPixel(2, 0, Color(7.0e1F, 8.0e1F, 9.0e1F))
        img.setPixel(0, 1, Color(1.0e2F, 2.0e2F, 3.0e2F))
        img.setPixel(1, 1, Color(4.0e2F, 5.0e2F, 6.0e2F))
        img.setPixel(2, 1, Color(7.0e2F, 8.0e2F, 9.0e2F))

        img.writePFM(streamBe, ByteOrder.BIG_ENDIAN)
        img.writePFM(streamLe, ByteOrder.LITTLE_ENDIAN)

        val referenceBe = byteArrayOfInts(
            0x50, 0x46, 0x0a, 0x33, 0x20, 0x32, 0x0a, 0x31, 0x2e, 0x30, 0x0a, 0x42,
            0xc8, 0x00, 0x00, 0x43, 0x48, 0x00, 0x00, 0x43, 0x96, 0x00, 0x00, 0x43,
            0xc8, 0x00, 0x00, 0x43, 0xfa, 0x00, 0x00, 0x44, 0x16, 0x00, 0x00, 0x44,
            0x2f, 0x00, 0x00, 0x44, 0x48, 0x00, 0x00, 0x44, 0x61, 0x00, 0x00, 0x41,
            0x20, 0x00, 0x00, 0x41, 0xa0, 0x00, 0x00, 0x41, 0xf0, 0x00, 0x00, 0x42,
            0x20, 0x00, 0x00, 0x42, 0x48, 0x00, 0x00, 0x42, 0x70, 0x00, 0x00, 0x42,
            0x8c, 0x00, 0x00, 0x42, 0xa0, 0x00, 0x00, 0x42, 0xb4, 0x00, 0x00
        )

        val referenceLe = byteArrayOfInts(
            0x50, 0x46, 0x0a, 0x33, 0x20, 0x32, 0x0a, 0x2d, 0x31, 0x2e, 0x30, 0x0a,
            0x00, 0x00, 0xc8, 0x42, 0x00, 0x00, 0x48, 0x43, 0x00, 0x00, 0x96, 0x43,
            0x00, 0x00, 0xc8, 0x43, 0x00, 0x00, 0xfa, 0x43, 0x00, 0x00, 0x16, 0x44,
            0x00, 0x00, 0x2f, 0x44, 0x00, 0x00, 0x48, 0x44, 0x00, 0x00, 0x61, 0x44,
            0x00, 0x00, 0x20, 0x41, 0x00, 0x00, 0xa0, 0x41, 0x00, 0x00, 0xf0, 0x41,
            0x00, 0x00, 0x20, 0x42, 0x00, 0x00, 0x48, 0x42, 0x00, 0x00, 0x70, 0x42,
            0x00, 0x00, 0x8c, 0x42, 0x00, 0x00, 0xa0, 0x42, 0x00, 0x00, 0xb4, 0x42
        )

        assertEquals(referenceBe.size, streamBe.toByteArray().size){"Error: Bytearrays (Big Endian) have different size"}
        assertArrayEquals(streamBe.toByteArray(), referenceBe){"Error: Bytearrays (Big Endian) have different elements"}

        assertEquals(referenceLe.size, streamLe.toByteArray().size){"Error: Bytearrays (Little Endian) have different size"}
        assertArrayEquals(streamLe.toByteArray(), referenceLe){"Error: Bytearrays (Little Endian) have different elements"}

    }

    @Test
    fun luminosity() {
        val col1 = Color(1.0F, 2.0F, 3.0F)
        val col2 = Color(9.0F, 5.0F, 7.0F)

        assert(are_close(2.0F, col1.luminosity()))
        assert(are_close(7.0F, col2.luminosity()))
    }

    @Test
    fun averageLuminosity() {
        val img = HdrImage(2, 1)
        img.setPixel(0, 0, Color(5F, 10F, 15F)) //luminosity 10
        img.setPixel(1, 0, Color(500F, 1000F, 1500F)) //luminosity 1000

        assert(are_close(100F, img.averageLuminosity(0.0F)))

    }

    @Test
    fun normalizeImage() {
        val img = HdrImage(2, 1)
        img.setPixel(0, 0, Color(5F, 10F, 15F)) //luminosity 10
        img.setPixel(1, 0, Color(500F, 1000F, 1500F)) //luminosity 1000

        img.normalizeImage(1000F, 100F)
        assert(img.getPixel(0, 0).areClose(Color(0.5e2F, 1.0e2F, 1.5e2F)))
        assert(img.getPixel(1, 0).areClose(Color(0.5e4F, 1.0e4F, 1.5e4F)))
    }

    @Test
    fun clampImage() {
        val img = HdrImage(2, 1)
        img.setPixel(0, 0, Color(0.5e1f, 1.0e1f, 1.5e1f))
        img.setPixel(1, 0, Color(0.5e3f, 1.0e3f, 1.5e3f))
        img.clampImage()

        for (pixel in img.pixels){
            assert(pixel.r in 0.0..1.0)
            assert(pixel.g in 0.0..1.0)
            assert(pixel.b in 0.0..1.0)
        }
    }

    @Test
    fun writeLdrTest(){
        val img = HdrImage(16, 16)
        var i = 1f
        var j = 256f
        for (y in img.height-1 downTo 0){
            for (x in 0..<img.width){
                img.setPixel(x,y, Color(141f,50f,122f))
                i += 1f
                j -= 1f
            }
        }

        val stream = FileOutputStream("test.pfm")
        img.writePFM(stream, ByteOrder.BIG_ENDIAN)
        img.writeLdrImage("png",1f, "mix.png")
    }
}


