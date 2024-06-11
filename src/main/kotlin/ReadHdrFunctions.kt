import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder


/**
 *These functions are used to read a PFM file
 */


/**
 * Returns a Floating-point number from an array of 4 bytes for a given Endianness
 */
fun readFloat(stream: InputStream, order: ByteOrder): Float {

    val buffer = ByteArray(4)
    stream.read(buffer)
    val translator = ByteBuffer.wrap(buffer)

    try {
        translator.order(order)
    } catch (e: InvalidPfmFileFormat) {
        println("Error:")
    }

    return translator.float
}

/**
 * Reads line of file until \n or the end of file and returns as string
 **/
fun readLine(stream: InputStream): String {
    val result = ByteArrayOutputStream()
    while (true) {
        val curByte = stream.read()
        if (curByte == -1 || curByte == '\n'.code) break
        result.write(curByte)
    }
    return result.toString("Ascii")
}

/**
 *  Gets width and height of an HdrImage from a PFM file
 */
fun parseImageSize(line: String): Array<Int> {
    val elements = line.split(" ")
    val dimensions = arrayOf(0, 0)
    if (elements.size != 2) run {
        throw (InvalidPfmFileFormat("invalid image size specification"))
    }

    dimensions[0] = elements[0].toInt()
    dimensions[1] = elements[1].toInt()

    if ((dimensions[0] < 0) || (dimensions[1] < 0)) {
        throw (InvalidPfmFileFormat("invalid width/height"))
    }

    return dimensions
}

/**
 * Decodes the endianness of a PFM file
 */
fun parseEndianness(line: String): ByteOrder {
    var value = 0F
    try {
        value = line.toFloat()
    } catch (e: InvalidPfmFileFormat) {
        println("Missing endianness specification")
    }

    if (value > 0) {
        return ByteOrder.BIG_ENDIAN
    }
    if (value < 0) {
        return ByteOrder.LITTLE_ENDIAN
    } else {
        throw (InvalidPfmFileFormat("invalid endianness specification, it cannot be zero"))
    }
}

/**
 * Creates an HdrImage from a PFM stream
 */
fun readPfmImage(stream: InputStream): HdrImage {

    // Checks that Pfm file starts with "PF"
    val magic = readLine(stream)
    if (magic != "PF") throw InvalidPfmFileFormat("Invalid magic in pfm file")

    // Reads width and length and save in dimensions
    val imageSize = readLine(stream)
    val dimensions = parseImageSize(imageSize)

    //reads endianness line and save in endianness
    val endiannessLine = readLine(stream)
    val endianness = parseEndianness(endiannessLine)


    // Now reads all the Colors of each pixel in result
    val result = HdrImage(dimensions[0], dimensions[1])
    for (y in dimensions[1] - 1 downTo 0) {
        for (x in 0 until dimensions[0]) {
            val r = readFloat(stream, endianness)
            val g = readFloat(stream, endianness)
            val b = readFloat(stream, endianness)
            result.setPixel(x, y, Color(r, g, b))
        }
    }
    return result
}

