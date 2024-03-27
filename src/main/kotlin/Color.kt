/**
Class Color
**/

data class Color(var r:Float, var g:Float, var b:Float) {
    /**
    Constructor for black color
     **/
    constructor() : this(0.0F, 0F, 0.0F);
    /**
     * Print the 3 components of the color
     */
    fun print(){
        println("Color(r=${this.r}, g=${this.g}, b=${this.b}")
    }
    /**
    Sum of two colors
     **/
    operator fun plus(other: Color): Color {
        return Color(r + other.r, g + other.g, b + other.b)
    }


    /**
     * Operator == for color class
     */
    override fun equals(other: Any?): Boolean { //override operatore logico ==
        /**
         * override == function
         */

        if (other is Color) {
            return ((this.r == other.r) and (this.g == other.g) and (this.b == other.b))

        }
        return false
    }

    /**
     * Boolean to understand if two colors are close
     */
    fun areClose(other: Color): Boolean {
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
     * "*" operator with float parameter
     */
    operator fun times(other:Float):Color{
        return Color(r*other,g*other,b*other)
    }

    /**
     * Product with another color
     */
    fun colorProduct(other: Color): Color {
        return Color(r * other.r, g * other.g, b * other.b)
    }
    /**
     * "*" operator with float parameter
     */
    operator fun times(other:Color):Color{
        return Color(r*other.r, g*other.g,b*other.b)
    }

    /**
     * Evaluate luminosity of a color with Shirley & Morley formula
     */
    fun luminosity(): Float {
        return (maxOf(maxOf(r, g), b) + minOf(minOf(r, g), b)) / 2
    }
}

