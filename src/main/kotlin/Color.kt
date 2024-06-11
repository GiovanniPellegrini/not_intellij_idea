/**
 *Color Class: Represents a color with RGB components
 * 
 *@param r: red component
 *@param g: green component
 *@param b: blue component
 *
 *@Default Constructor: r=0, g=0, b=0
 **/

data class Color(var r: Float = 0f, var g: Float = 0f, var b: Float = 0f) {
    /**
     * Sum operator of two colors
     **/
    operator fun plus(other: Color): Color {
        return Color(r + other.r, g + other.g, b + other.b)
    }

    /**
     * Operator == for Colors. It uses the are_close function
     */
    override fun equals(other: Any?): Boolean {
        if (other is Color) {
            return (areClose(r, other.r) and (areClose(this.g, other.g)) and (areClose(this.b, other.b)))

        }
        return false
    }

    /**
     * Returns Boolean if two colors are close
     */
    fun areClose(other: Color): Boolean {
        return (areClose(r, other.r) &&
                areClose(g, other.g) &&
                areClose(b, other.b))
    }


    /**
     * Product each component with a scalar
     */
    fun scalarProduct(scalar: Float): Color {
        return Color(r * scalar, g * scalar, b * scalar)
    }

    /**
     * Times operator with float parameter
     */
    operator fun times(other: Float): Color {
        return this.scalarProduct(other)
    }

    /**
     * Product with another color
     */
    fun colorProduct(other: Color): Color {
        return Color(r * other.r, g * other.g, b * other.b)
    }

    /**
     * Times operator with float parameter
     */
    operator fun times(other: Color): Color {
        return this.colorProduct(other)
    }

    /**
     * Evaluate luminosity of a color with Shirley & Morley formula
     */
    fun luminosity(): Float {
        return (maxOf(r, g, b) + minOf(r, g, b)) / 2f
    }
}

