import java.awt.image.BufferedImage
import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.imageio.ImageIO
import kotlin.math.*


fun clamp(x:Float):Float{
    return x/(x+1)
}

/**
 * HDR image Class
 *
 * @property width: image width
 * @property height: image height
 * @property pixels: array of colors
 *
 * @default constructor: Create empty Hdr image
 */
data class HdrImage (var width: Int = 0,
                     var height: Int = 0,
                     var pixels: Array<Color> = Array(size = width * height) {Color()}){

    /**
     * Get color of a pixel
     */
    fun getPixel(x: Int, y: Int): Color {
        assert(validCoordinates(x,y)){"Error: invalid coordinates"}
        return pixels[y * width + x]
    }

    /**
     * Set Color of a pixel
     */
    fun setPixel(x: Int, y: Int, newColor: Color) {
        assert(validCoordinates(x,y)){"Error: invalid coordinates"}
        pixels[y * width + x] = newColor
    }

    /**
     * Check if coordinates are valid
     */
    fun validCoordinates(x:Int, y:Int): Boolean{
        return ((x>=0) and (x<width) and (y>=0) and (y<height))
    }

    /**
     * convert and write Float32 into a bytearray
     */
    private fun writeFloat(stream: OutputStream, value : Float, order: ByteOrder){
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

        stream.write(header.toByteArray())
        // pixel writing starts from bottom left
        for (y in height-1 downTo 0){
            for (x in 0..<width){
                val color = getPixel(x,y)
                writeFloat(stream, color.r, order)
                writeFloat(stream, color.g, order)
                writeFloat(stream, color.b, order)
            }
            val progress = ((height-y).toFloat() / this.height.toFloat()) * 100
            val status = "#".repeat(progress.toInt())
            val remaining = " ".repeat(100 - progress.toInt())
            print("writing PFM image progress: [${status}${remaining}] ${round(progress)}% \r")
        }
        println("\n PFM image saved")
    }

    /**
     * Calculate the average luminosity using the logarithmic average formula.
     */
    fun averageLuminosity(delta: Float =1e-10F):Float{
        var sum = 0.0f
        for (element in pixels){
            sum+= log10(delta + element.luminosity())
        }
        return 10f.pow(sum / pixels.size)
    }

    /**
    normalize each color multiplying with factor/luminosity.
    If not specified, the luminosity is that of Shirley and Morley's formula
    **/
    fun normalizeImage(factor: Float, luminosity: Float?=null){
        val lum = luminosity ?: averageLuminosity()
        for(i in pixels.indices){
            pixels[i] = pixels[i].scalarProduct(factor/lum)
        }
    }

    /**
     * Each value of color is clamped from 0 to 1, to treat luminous spots
     */
    fun clampImage(){
        for(pixel in pixels){
            pixel.r = clamp(pixel.r)
            pixel.g = clamp(pixel.g)
            pixel.b = clamp(pixel.b)
        }
    }

    /**
     * convert from HDR to LDR and saves on disk the image
     */
    fun writeLdrImage(format: String, gamma:Float = 1.0f, outputFilename: String){
        val bufferedImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB)
        for(y in 0 until this.height){
            for(x in 0 until this.width){
                val curColor = this.getPixel(x,y)
                val red = (255 * curColor.r.pow(1/gamma)).toInt()
                val green = (255 * curColor.g.pow(1/gamma)).toInt()
                val blue = (255 * curColor.b.pow(1/gamma)).toInt()
                val rgb = red.shl(16) + green.shl(8) + blue
                bufferedImage.setRGB(x, y, rgb)
            }
            val progress = (y.toFloat() / this.height.toFloat()) * 100
            val status = "#".repeat(progress.toInt())
            val remaining = " ".repeat(99 - progress.toInt())
            print("writing LDR image progress: [${status}${remaining}] ${round(progress)}% \r")
        }

        val outputFile = File(outputFilename)
        try {
            ImageIO.write(bufferedImage, format, outputFile)
            println("\n image $outputFilename saved")
        } catch (e: Exception) {
            println("Error: image not saved")
        }

    }

}

