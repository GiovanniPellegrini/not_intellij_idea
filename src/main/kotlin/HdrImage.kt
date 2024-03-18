import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Class of HDR image
 */
class HdrImage (var width: Int,var height: Int) {
    /**
     * Initialize image with dimension width*height
     */
    var pixels = Array(width * height) { Color(1.0F, 1.0F, 1.0F) }

    /**
     * Get color of a pixel
     */
    fun getPixel(x: Int, y: Int): Color {
        return pixels[y * width + x]
    }

    /**
     * Set Color of a pixel
     */
    fun setPixel(x: Int, y: Int, new_color: Color) {
        pixels[y * width + x] = new_color
    }

    /**
     * convert and write Float32 into a bytearray
     */
    fun writeFloat(stream: OutputStream, value : Float, order: ByteOrder){

        val bytes = ByteBuffer.allocate(4).putFloat(value).array() /* ByteArray written in Big Endian */

        if (order == ByteOrder.LITTLE_ENDIAN) {
            bytes.reverse()
        }

        stream.write(bytes)
    }

    /**
     * write a PFM file from a pixel array
     */

    fun writePFM(image: HdrImage, stream: OutputStream, order: ByteOrder){
        val endiannessStr = if (order == ByteOrder.LITTLE_ENDIAN) "-1.0" else "1.0"
        val header = "PF\n $image.width $image.height\n$endiannessStr\n"  //per convertirla in binario .toByteArray()

        val fos = FileOutputStream("out.pfm")
        fos.use {
            outStream -> outStream.write(header.toByteArray())
            for (y in image.height downTo 0){
                for (x in 0..image.width){
                    var color = image.getPixel(x,y)
                    writeFloat(stream, color.r, order)
                    writeFloat(stream, color.g, order)
                    writeFloat(stream, color.b, order)
                }
            }
        }
    }


}