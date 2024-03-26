import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.swing.plaf.InputMapUIResource

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
        val curbyte=stream.read()
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
fun parseEndianness(line: String): ByteOrder {
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

/**
 * Read a Pfm image
 */
fun readPfmImage(stream:InputStream): HdrImage{

    // Check that Pfm file starts with "PF"
    val magic=readline(stream)
    if(magic!="PF") throw InvalidPfmFileFormat("Invalid magic in pfm file")

    // Read width and length and save in dimensions
    val imageSize=readline(stream)
    val dimensions =parseImageSize(imageSize)
    println("dimensioni immagine: ${dimensions[0]} x ${dimensions[1]}")

    //read endianness line and save in endianness
    val endiannessline= readline(stream)
    val endianness=parseEndianness(endiannessline)


    // Now read all the Color of each pixel in result
    val result=HdrImage(dimensions[0],dimensions[1])
    val range = 0 until dimensions[1]
    for(y in range.reversed()){
        for (x in 0 until  dimensions[0]){
           for (i in 0 until 3) {
               val r = readFloat(stream, endianness)
               val g= readFloat(stream,endianness)
               val b= readFloat(stream,endianness)
               result.setPixel(x,y,Color(r,g,b))
           }

        }
    }
    return result
}

