import java.io.File
import java.io.FileInputStream
import java.io.IOException
import kotlin.math.abs
import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple

class Tracer: CliktCommand() {
    override fun run() = Unit
}

class Convert(): CliktCommand() {
    private val args: List<String> by argument().multiple()
    /** Main arguments:
    1. PFM input file
    2. parameter "a"
    3. gamma value
    3. png file output name
     **/
    override fun run() {
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
            if (aValue <=0f || GammaValue<=0f){
                throw Exception("parameters 'a' and 'gamma' must be strictly positive")
            }

            val OutputFile = File(args[3])
            if (!OutputFile.name.endsWith(".png")) {
                throw IllegalArgumentException("Last argument must have .png extension")
            }
        } catch (e: NumberFormatException) {
            println("Invalid Format parameters, please enter the parameters in the following order : " +
                    "\n- Input .pfm filename\n" +
                    "-parameter 'a' (Float)\n" +
                    "-parameter 'gamma' (Float)\n" +
                    "- Output .png filename ")

            aValue = 1F
            GammaValue = 1F
        }

        val sampleStream = FileInputStream(args[0])
        val sampleImage = readPfmImage(sampleStream)
        sampleImage.normalizeImage(aValue)
        sampleImage.clampImage()

        sampleImage.writeLdrImage("png", GammaValue, "prova.png")
    }
}

class Hello: CliktCommand() {
    override fun run() {
        echo("Hello World!")
    }
}


fun main(args: Array<String>) = Tracer().subcommands(Convert(), Hello()).main(args)



fun are_close(x: Float, y: Float, epsilon: Float = 1.0E-5F): Boolean {
    return (abs(x-y)<epsilon)
}

