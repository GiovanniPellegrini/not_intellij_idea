/**
 * Interface for Pigment, represent the function that returns the color of a point
 * parametrized by a 2D vector of a surface
 */
interface Pigment {
    fun getColor(vec2d: Vec2d): Color
}