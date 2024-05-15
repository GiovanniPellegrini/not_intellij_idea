/**
 * Interface for Pigment, represent the function that returns the color of a point
 * parametrized by a 2D vector of a surface
 */
interface Pigment {
    fun getColor(vec2d: Vec2d): Color
}

/**
 * uniform pigment, return the same color for all points
 */
class UniformPigment(private val color: Color) : Pigment {

    constructor(): this(Color())

    override fun getColor(vec2d: Vec2d): Color {
        return color
    }
}
/**
 * checkered pigment, return one of two colors depending on the position of the point
 */
class CheckeredPigment(private val color1: Color, private val color2: Color, private val steps: Int) : Pigment {
    override fun getColor(vec2d: Vec2d): Color {
        val u = (vec2d.u * steps).toInt()
        val v = (vec2d.v * steps).toInt()
        return if ((u % 2) == (v % 2)) color1 else color2
    }
}
/**
 * Image pigment, return the color of a pixel of an image
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