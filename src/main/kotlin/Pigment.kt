import kotlin.math.floor

/**
 * Pigment interface: used to define the color of a point
 *
 * @property getColor: Return the color of a point on a surface

 */
interface Pigment {
    /**
     * Return the color of a point on a surface
     */
    fun getColor(vec2d: Vec2d): Color
}

/**
 * Uniform pigment class: derived from Pigment return the same color for all points
 *
 * @property color: Color of the pigment
 */
class UniformPigment(val color: Color = Color()) : Pigment {
    override fun getColor(vec2d: Vec2d): Color {
        return color
    }
}

/**
 * Checkered pigment class: derived from Pigment return a checkered pattern of two colors
 *
 * @property color1: First color of the checkered pattern
 * @property color2: Second color of the checkered pattern
 * @property steps: Number of steps of the checkered pattern
 *
 * @constructor default: color1 = Color(), color2 = Color(1f,1f,1f), steps = 3
 */
class CheckeredPigment(val color1: Color = Color(), val color2: Color = Color(1f, 1f, 1f), val steps: Int = 3) :
    Pigment {
    override fun getColor(vec2d: Vec2d): Color {
        val u = floor(vec2d.u * steps).toInt()
        val v = floor(vec2d.v * steps).toInt()
        return if ((u % 2) == (v % 2)) color1 else color2
    }
}

/**
 * Image pigment class: derived from Pigment return the color of a point from an HDRimage
 *
 * @property image: HDRimage used to get the color of a point
 */
class ImagePigment(private val image: HdrImage) : Pigment {
    override fun getColor(vec2d: Vec2d): Color {
        var col = (vec2d.u * image.width).toInt()
        var row = (vec2d.v * image.height).toInt()

        if (col >= this.image.width) {
            col = this.image.width - 1;
        }

        if (row >= this.image.height) {
            row = this.image.height - 1;
        }
        return image.getPixel(col, row)
    }
}