import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class InvalidPfmFileFormat(message: String) : Exception("prova exception")

/**
 * Return a Floating-point number from an array of 4 bytes for a given Endianness
 */
fun readFloat(stream: InputStream, order: ByteOrder): Float {

    val buffer = ByteArray(4)
    stream.read(buffer)
    val translator = ByteBuffer.wrap(buffer)

    try{
        translator.order(order)
    }catch (e: InvalidPfmFileFormat){
        println("Error:")
    }

    return translator.float
}
/**
Read a lines of file until /n and return that as string
**/
fun readline(stream:InputStream):String{
    val result= ByteArrayOutputStream()
    while (true) {
        var curbyte=stream.read()
        if (curbyte==-1 || curbyte== '\n'.code)
            break
        result.write(curbyte)
    }
    return result.toString("Ascii")
}


/**
 *  Get width and height of an HdrImage from a PFM file
 */
fun parseImageSize(line: String): Array<Int> {
    val elements = line.split(" ")
    val dimensions = arrayOf(0,0)
    if (elements.size != 2)run {
        throw (InvalidPfmFileFormat("invalid image size specification"))
    }

    dimensions[0] = elements[0].toInt()
    dimensions[1] = elements[1].toInt()

    if ((dimensions[0] < 0) || (dimensions[1] < 0)){
            throw (InvalidPfmFileFormat("invalid width/height"))
    }

    return dimensions
}

/**
 * decoding the endianness of a PFM file
 */
fun parseEndianness(line: String): ByteOrder? {
    var value = 0F
    try {
        value = line.toFloat()
    }catch (e: InvalidPfmFileFormat){
        println("Missing endianness specification")
    }

    if (value>0){
        return ByteOrder.BIG_ENDIAN
    }
    if (value<0){
        return ByteOrder.LITTLE_ENDIAN
    }
    else{
        throw (InvalidPfmFileFormat("invalid endianness specification, it cannot be zero"))
    }

}


