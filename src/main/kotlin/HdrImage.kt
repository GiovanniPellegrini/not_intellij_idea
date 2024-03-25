import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Math.pow
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.log
import kotlin.math.*


fun clamp(x:Float):Float{
    return x/(x+1)
}

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

    fun writePFM(stream: OutputStream, order: ByteOrder){
        val endiannessStr = if (order == ByteOrder.LITTLE_ENDIAN) "-1.0" else "1.0"
        val header = "PF\n${this.width} ${this.height}\n$endiannessStr\n"

        stream.use {
            outStream -> outStream.write(header.toByteArray())
            // pixel writing starts from bottom left
            for (y in height-1 downTo 0){
                for (x in 0..<width){
                    val color = getPixel(x,y)
                    writeFloat(stream, color.r, order)
                    writeFloat(stream, color.g, order)
                    writeFloat(stream, color.b, order)
                }
            }
        }
    }


    /**
     * Calculate the average luminosity using the logarithmic average formula.
     */
    fun averageLuminosity(delta: Float =1e-10F):Float{
        var sum:Float= 0.0F
        for (i in 0 until  pixels.size ){
            sum+= log10(delta+pixels[i].luminosity())
        }
        return 10.0.pow(sum / pixels.size.toDouble()).toFloat()
    }

    /**
    normalize each color multiplying with factor/luminosity.
    If not specified, the luminosity is that of Shirley and Morley's formula
    **/
    fun normalizeImage(factor:Float,  luminosity: Float?=null){
        val lum=luminosity ?: averageLuminosity()
        for(i in 0 until  pixels.size){
            pixels[i]=pixels[i].scalarProduct(factor/lum)
        }
    }

    /**
     * Each value of color is clamped from 0 to 1,  to treat luminous spots
     */
    fun clampImage(){
        for(pixel in pixels){
            pixel.r=clamp(pixel.r)
            pixel.g=clamp(pixel.g)
            pixel.b=clamp(pixel.b)
        }
    }

}

