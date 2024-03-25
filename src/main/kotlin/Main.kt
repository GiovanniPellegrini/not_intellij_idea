import java.io.File
import java.io.IOException
import kotlin.math.abs

/* Main arguments:
    1. PFM input file
    2. parameter "a"
    3. gamma value
    3. png file output name
 */
fun main(args: Array<String>) {
    if (args.size != 4) {
        throw IOException(
            "Invalid input parameters, please enter the parameters in the following order : " +
                    "\n- Input .pfm filename\n" +
                    "- parameter 'a' (Float)\n" +
                    "- parameter 'gamma' (Float)\n" +
                    "- Output .png filename "
        )
    }

    var aValue: Float
    var GammaValue: Float

    //defining main parameters values
    try {
        val InputFile = File(args[0])
        if (!InputFile.name.endsWith(".pfm")) {
            throw IllegalArgumentException("First argument must have .pfm extension")
        }

        aValue = args[1].toFloat()
        GammaValue = args[2].toFloat()

        val OutputFile = File(args[3])
        if (!OutputFile.name.endsWith(".png")) {
            throw IllegalArgumentException("Last argument must have .png extension")
        }
    } catch (e: NumberFormatException) {
        println("Invalid Format parameters, please enter the parameters in the following order : " +
                "\n Input .pfm filename\n" +
                "-parameter 'a' (Float)\n" +
                "-parameter 'gamma' (Float)\n" +
                "- Output .png filename ")

        aValue = 0F
        GammaValue = 0F
    }

    println(aValue + GammaValue)
}


fun are_close(x: Float, y: Float, epsilon: Float = 1.0E-5F): Boolean {
    return (abs(x-y)<epsilon)
}