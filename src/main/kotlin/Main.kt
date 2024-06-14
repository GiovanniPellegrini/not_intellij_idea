import java.io.File
import java.io.FileInputStream
import java.io.IOException
import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import java.io.FileOutputStream
import java.nio.ByteOrder
import compiler.*
import java.io.*

class Tracer : CliktCommand(help = "Welcome to our raytracer") {
    override fun run() = Unit
}

class Pfm2Png : CliktCommand(printHelpOnEmptyArgs = true, help = "Convert a PFM file to a PNG image") {
    private val args: List<String> by argument(
        help = "- .pfm filename Input,\n- parameter 'a' (Float),\n" +
                "- parameter 'gamma' (Float),\n" +
                "- .png filename Output"
    ).multiple()

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
        var gammaValue: Float

        //defining main parameters values
        try {
            val inputFile = File(args[0])
            if (!inputFile.name.endsWith(".pfm")) {
                throw IllegalArgumentException("First argument must have .pfm extension")
            }

            aValue = args[1].toFloat()
            gammaValue = args[2].toFloat()
            if (aValue <= 0f || gammaValue <= 0f) {
                throw IllegalArgumentException("parameters 'a' and 'gamma' must be strictly positive")
            }

            val outputFile = File(args[3])
            if (!outputFile.name.endsWith(".png")) {
                throw IllegalArgumentException("Last argument must have .png extension")
            }
        } catch (e: NumberFormatException) {
            println(
                "Invalid Format parameters, please enter the parameters in the following order : " +
                        "\n- Input .pfm filename\n" +
                        "-parameter 'a' (Float)\n" +
                        "-parameter 'gamma' (Float)\n" +
                        "- Output .png filename "
            )

            aValue = 1F
            gammaValue = 1F
        }

        val sampleStream = FileInputStream(args[0])
        val sampleImage = readPfmImage(sampleStream)
        sampleImage.normalizeImage(aValue)
        sampleImage.clampImage()

        sampleImage.writeLdrImage("png", gammaValue, args[3])
    }
}

class Demo : CliktCommand(printHelpOnEmptyArgs = true, help = "Create a demo image with 10 spheres from demo txt") {
    private val args: List<String> by argument(
        help = "- rotation angle of the camera (Float) \n" +
                "- .pfm filename Output \n" +
                "- Output .png filename (default a=1 gamma=1) \n" +
                "- Type of camera (Perspective or Orthogonal) \n" +
                "- save .pfm Output (optional, true or false) \n"
    ).multiple()

    override fun run() {
        val stream = InStream(stream = FileReader("src/main/kotlin/examples/demo.txt"), fileName = "demo.txt")
        val scene = Scene()
        scene.parseScene(stream)

        val camera = when (args[3]) {
            "perspective" -> {
                PerspectiveCamera(
                    transformation = Rotation(Vector(0f, 0f, 1f), args[0].toFloat()) * Translation(
                        Vector(
                            -2f,
                            0f,
                            0f
                        )
                    )
                )
            }

            "orthogonal" -> {
                OrthogonalCamera(
                    transformation = Rotation(
                        Vector(0f, 0f, 1f),
                        args[0].toFloat()
                    ) * Translation(Vector(-2f, 0f, 0f))
                )
            }

            else -> {
                throw IllegalArgumentException("Illegal argument for camera, must be perspective or orthogonal")
            }
        }

        val image = HdrImage(720, 720)
        val tracer = ImageTracer(image, camera)
        val renderer = OnOffRenderer(scene.world)
        tracer.fireAllRays(renderer::render)
        image.normalizeImage(1f)
        image.clampImage()
        if(args[4] == "true") {
            val outputStream = FileOutputStream(args[1])
            image.writePFM(outputStream, ByteOrder.BIG_ENDIAN)
        }
        image.writeLdrImage("png", 1f, args[2])

    }
}

class Render : CliktCommand(printHelpOnEmptyArgs = true, help = "Create a demo image using pathTracing algorithm") {
    private val args: List<String> by argument(
        help = "- camera angle ," +
                "\n- maxDepth parameter (Int),\n" +
                "- russianRoulette limit parameter (Int),\n" +
                "- number of rays parameter (Int),\n" +
                "- Output .png filename"
    ).multiple()

    override fun run() {

        val sphere1 = Sphere(
            scalingTransformation(Vector(0.6f, 0.6f, 0.6f)) * Translation(Vector(0.8f, 1.3f, -0.5f)),
            Material(emittedRad = UniformPigment(Color(230f, 0f, 0f)))
        )
        val plane1 = Plane(
            transformation = Translation(Vector(0f, 0f, -1f)),
            Material(emittedRad = CheckeredPigment(Color(170f, 0f, 255f), color2 = Color(0.1f, 0.2f, 0.5f), steps = 4))
        )
        val mirror = Sphere(
            scalingTransformation(Vector(0.4f, 0.4f, 0.4f)) * Translation(Vector(4f, -1.5f, -2f)),
            Material(brdf = SpecularBRDF(UniformPigment(Color(0.2f, 0.4f, 0.6f))))
        )
        val sky = Sphere(
            transformation = scalingTransformation(Vector(200f, 200f, 200f)) * Translation(Vector(0f, 0f, 0.4f)),
            material = Material(
                brdf = DiffusionBRDF(UniformPigment(Color(0f, 0f, 0f))),
                emittedRad = UniformPigment(Color(0f, 255f, 255f))
            )

        )
        val world = World()
        world.addShape(sphere1)
        world.addShape(plane1)
        world.addShape(mirror)
        world.addShape(sky)

        val image = HdrImage(1080, 1080)

        val camera = PerspectiveCamera(transformation = Rotation(Vector(0f, 0f, 1f), args[0].toFloat()))
        val tracer = ImageTracer(image, camera)
        val renderer = PathTracer(world=world, maxDepth = args[1].toInt(), russianRouletteLimit = args[2].toInt(), numberOfRays = args[3].toInt())
        tracer.fireAllRays(renderer::render)
        image.normalizeImage(1f)
        image.clampImage()
        val stream = FileOutputStream("output.pfm")
        println("Writing PFM file")
        image.writePFM(stream, ByteOrder.BIG_ENDIAN)
        println("Writing PNG file")
        image.writeLdrImage("png", 2.2f, args[4])
    }
}

fun main(args: Array<String>) = Tracer().subcommands(Pfm2Png(), Demo(), Render()).main(args)



