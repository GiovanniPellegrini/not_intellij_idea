/**
Class Color
**/

class Color(var r:Float, var g:Float, var b:Float) {
    /**
    Constructor for black color
     **/
    constructor() : this(0.0F, 0F, 0.0F);
    /**
    Sum of two colors
     **/
    fun sum(other: Color): Color {
        return Color(r + other.r, g + other.g, b + other.b)
    }

    /**
     * Boolean to understand if two colors are close
     */
    fun areCloseColor(other: Color): Boolean {
        return (are_close(r, other.r) &&
                are_close(g, other.g) &&
                are_close(b, other.b))
    }


    /**
     * Product each component with a scalar
     */
    fun scalarProduct(scalar: Float): Color {
        return Color(r * scalar, g * scalar, b * scalar)
    }

    /**
     * Product with another color
     */
    fun colorProduct(other: Color): Color {
        return Color(r * other.r, g * other.g, b * other.b)
    }

    /**
     * Evaluate luminosity of a color with Shirley & Morley formula
     */
    fun luminosity(): Float {
        return (maxOf(maxOf(r, g), b) + minOf(minOf(r, g), b)) / 2
    }
}