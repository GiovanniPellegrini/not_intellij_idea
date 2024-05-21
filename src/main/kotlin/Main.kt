import java.io.File
import java.io.FileInputStream
import java.io.IOException
import kotlin.math.abs
import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import java.io.FileOutputStream
import java.nio.ByteOrder


class Tracer: CliktCommand() {
    override fun run() = Unit
}

class Convert: CliktCommand() {
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
        var gammaValue: Float

        //defining main parameters values
        try {
            val inputFile = File(args[0])
            if (!inputFile.name.endsWith(".pfm")) {
                throw IllegalArgumentException("First argument must have .pfm extension")
            }

            aValue = args[1].toFloat()
            gammaValue = args[2].toFloat()
            if (aValue <=0f || gammaValue<=0f){
                throw Exception("parameters 'a' and 'gamma' must be strictly positive")
            }

            val outputFile = File(args[3])
            if (!outputFile.name.endsWith(".png")) {
                throw IllegalArgumentException("Last argument must have .png extension")
            }
        } catch (e: NumberFormatException) {
            println("Invalid Format parameters, please enter the parameters in the following order : " +
                    "\n- Input .pfm filename\n" +
                    "-parameter 'a' (Float)\n" +
                    "-parameter 'gamma' (Float)\n" +
                    "- Output .png filename ")

            aValue = 1F
            gammaValue = 1F
        }

        val sampleStream = FileInputStream(args[0])
        val sampleImage = readPfmImage(sampleStream)
        sampleImage.normalizeImage(aValue)
        sampleImage.clampImage()

        sampleImage.writeLdrImage("png", gammaValue, "test.png")
    }
}

class Demo: CliktCommand() {
    private val args: List<String> by argument().multiple()
    override fun run() {
        val sphere1 = Sphere(scalingTransformation(Vector(0.1f,0.1f,0.1f))*Translation(Vector(3.0f,3.0f,3.0f)))
        val sphere2 = Sphere(scalingTransformation(Vector(0.1f,0.1f,0.1f))*Translation(Vector(3.0f,3.0f,-3.0f)))
        val sphere3 = Sphere(scalingTransformation(Vector(0.1f,0.1f,0.1f))*Translation(Vector(3.0f,-3.0f,3.0f)))
        val sphere4 = Sphere(scalingTransformation(Vector(0.1f,0.1f,0.1f))*Translation(Vector(-3.0f,3.0f,3.0f)))
        val sphere5 = Sphere(scalingTransformation(Vector(0.1f,0.1f,0.1f))*Translation(Vector(-3.0f,-3.0f,-3.0f)))
        val sphere6 = Sphere(scalingTransformation(Vector(0.1f,0.1f,0.1f))*Translation(Vector(-3.0f,-3.0f,3.0f)))
        val sphere7 = Sphere(scalingTransformation(Vector(0.1f,0.1f,0.1f))*Translation(Vector(-3.0f,3.0f,-3.0f)))
        val sphere8 = Sphere(scalingTransformation(Vector(0.1f,0.1f,0.1f))*Translation(Vector(3.0f,-3.0f,-3.0f)))

        val world = World()
        world.add(sphere1)
        world.add(sphere2)
        world.add(sphere3)
        world.add(sphere4)
        world.add(sphere5)
        world.add(sphere6)
        world.add(sphere7)
        world.add(sphere8)
        val image = HdrImage(500,500)

        val camera = PerspectiveCamera(transformation = Rotation(Vector(0f,0f,1f) ,args[0].toFloat()))
        val tracer = ImageTracer(image,camera)
        val defaultColor = Color(75f, 60f, 66f)

        val onOff: (Ray) -> Color = { ray ->
            val intersection = world.rayIntersection(ray)

            if (intersection == null) {
                Color()
            } else {
                defaultColor
            }
        }

        tracer.fireAllRays(onOff)
        image.normalizeImage(0.1f)
        image.clampImage()
        val stream = FileOutputStream("output.pfm")
        image.writePFM(stream, ByteOrder.BIG_ENDIAN)
        image.writeLdrImage("png",1f, args[1])
    }
}

class MeshDemo: CliktCommand() {
    private val args: List<String> by argument().multiple()
    override fun run() {

        val mesh = TriangleMesh("path of the file .obj", transformation = Translation(Vector(1f,0f,0f))
                *scalingTransformation(Vector(0.5f,0.5f,0.5f)),
                material = Material(emittedRad = UniformPigment(Color(242f,140f,140f)))
        )
        val plane = Plane(transformation = Translation(Vector(0f,0f,-1f)),
            material = Material(emittedRad = CheckeredPigment(Color(0f,0f,255f),Color(255f,0f,0f),steps = 2)))
        val world = World()
        world.add(mesh)
        world.add(plane)

        val image = HdrImage(400,400)

        val camera = PerspectiveCamera(transformation = Rotation(Vector(0f,0f,1f) , 0f))
        val tracer = ImageTracer(image,camera)
        val renderer = FlatRenderer(world)
        tracer.fireAllRays(renderer::render)
        image.normalizeImage(1f, luminosity = 0.1f)
        image.clampImage()
        val stream = FileOutputStream("triangle.pfm")
        image.writePFM(stream, ByteOrder.BIG_ENDIAN)
        image.writeLdrImage("png",0.1f, args[1])
    }
}


fun main(args: Array<String>) = Tracer().subcommands(Convert(), Demo(), MeshDemo()).main(args)



fun are_close(x: Float, y: Float, epsilon: Float = 1.0E-5F): Boolean {
    return (abs(x-y)<epsilon)
}

