import java.io.FileInputStream
import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*

import java.io.FileOutputStream
import java.nio.ByteOrder
import compiler.*
import java.io.*

class NIJ : CliktCommand(
    help = "NIJ tracer") {
    override fun run() = Unit
}

class Pfm2Png : CliktCommand(printHelpOnEmptyArgs = true, help = "Convert a PFM file to a PNG image") {
    private val inputFile by option("-i", "--input", help = ".pfm filename Input").required()
    private val aValue by option("-a", "--aValue", help = "parameter 'a' (default=1)").float().default(1f)
    private val gammaValue by option("-g", "--gamma", help = "parameter 'gamma' (default=1)").float().default(1f)
    private val outputFile by option("-o", "--output", help = ".png filename Output").required()

    override fun run() {

        if (aValue <= 0f || gammaValue <= 0f) {
            throw IllegalArgumentException("parameters 'a' and 'gamma' must be strictly positive")
        }

        if (!inputFile.endsWith(".pfm")) {
            throw IllegalArgumentException("First argument must have .pfm extension")
        }

        if (!outputFile.endsWith(".png")) {
            throw IllegalArgumentException("Last argument must have .png extension")
        }

        val sampleStream = FileInputStream(inputFile)
        val sampleImage = readPfmImage(sampleStream)
        sampleImage.normalizeImage(aValue)
        sampleImage.clampImage()

        sampleImage.writeLdrImage("png", gammaValue, outputFile)
    }


}

class Demo : CliktCommand(printHelpOnEmptyArgs = true, help = "Create a demo image with 10 spheres from demo txt") {
    private val rotationAngle by option("-r", "--rotation", help = "rotation angle of the camera (default=0)").float()
        .default(0f)
    private val pfmOutput by option("-p", "--pfm", help = ".pfm filename Output").required()
    private val pngOutput by option("-o", "--output", help = "Output .png filename").required()
    private val savePfmOutput by option("-s", "--save", help = "save .pfm Output").convert { it.toBoolean() }
        .default(false)

    override fun run() {
        val stream = InStream(stream = FileReader("src/main/kotlin/examples/demo.txt"), fileName = "demo.txt")
        val scene = Scene()
        scene.parseScene(stream)

        val image = HdrImage(720, 720)

        val tracer = try {
            ImageTracer(image, scene.camera!!)
        } catch (e: Exception) {
            ImageTracer(
                image,
                camera = PerspectiveCamera(transformation = Rotation(Vector(0f, 0f, 1f), rotationAngle))
            )
        }

        val renderer = OnOffRenderer(scene.world)
        tracer.fireAllRays(renderer::render)
        image.normalizeImage(1f)
        image.clampImage()
        if (savePfmOutput) {
            val outputStream = FileOutputStream(pfmOutput)
            image.writePFM(outputStream, ByteOrder.BIG_ENDIAN)
        }
        image.writeLdrImage("png", 1f, pngOutput)
    }
}

class Render : CliktCommand(
    printHelpOnEmptyArgs = true, help = "Create a demo image with two different algorithm from a txt file"
) {
    private val algorithm by option(
        "-a",
        "--algorithm",
        help = "write the algorithm name (pathtracer, pointlighttracer), default pathtracer"
    ).default("pathtracer")
    private val inputFile by option("-i", "--input", help = ".txt filename Input").required()
    private val maxDepth by option("-m", "--maxDepth", help = "maxDepth (Int), default 3").int().default(3)
    private val russianRouletteLimit by option(
        "-r",
        "--russianRouletteLimit",
        help = "russianRouletteLimit (Int) default 2"
    ).int().default(2)
    private val numberOfRays by option("-n", "--numberOfRays", help = "numberOfRays (Int), default 15").int().default(15)
    private val imageWidth by option("-w", "--imageWidth", help = "imageWidth (Int), default 480").int().default(480)
    private val imageHeight by option("-h", "--imageHeight", help = "imageHeight (Int), default 480").int().default(480)
    private val pngOutput by option("-p", "--pngOutput", help = ".png filename Output").required()
    private val pfmOutput by option("-f", "--pfmOutput", help = ".pfm filename Output").required()
    private val antialiasing by option("-an", "--antialiasing", help = "Antialiasing (Boolean)").convert { it.toBoolean() }
        .default(false)
    private val raysForSide by option("-s", "--raysForSide", help = "Antialiasing number of rays for side (Int)").int()
        .default(2)

    override fun run() {
        val stream = InStream(stream = FileReader(inputFile), fileName = inputFile)
        val scene = Scene()
        scene.parseScene(stream)
        val image = HdrImage(imageWidth, imageHeight)
        val tracer = ImageTracer(image, scene.camera!!)

        val renderer = when (algorithm) {
            "pathtracer" -> {
                PathTracer(
                    world = scene.world,
                    maxDepth = maxDepth,
                    russianRouletteLimit = russianRouletteLimit,
                    numberOfRays = numberOfRays
                )
            }

            "pointlighttracer" -> {
                PointLightRenderer(world = scene.world)
            }

            else -> {
                throw IllegalArgumentException("Invalid algorithm name")
            }
        }

        if (!antialiasing) tracer.fireAllRays(renderer::render)
        else tracer.fireAllRays(renderer::render, raysForSide = raysForSide)
        image.normalizeImage(1.0f)
        image.clampImage()
        val outputStream = FileOutputStream(pfmOutput)
        println("Writing PFM file")
        image.writePFM(outputStream, ByteOrder.BIG_ENDIAN)
        println("Writing PNG file")
        image.writeLdrImage("png", 1f, pngOutput)
    }
}

fun main(args: Array<String>) = NIJ().subcommands(Pfm2Png(), Demo(), Render()).main(args)



