import kotlin.math.abs

/**
 *  Point Class: Represents a 3D point with float components
 * @param x: x component
 * @param y: y component
 * @param z: z component
 *
 * @Default Constructor: x=0, y=0, z=0
 */

data class Point(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f) {

    /**
     * Returns vector components as a string
     */
    override fun toString(): String {
        return "Point:<x:$x, y=$y, z=$z>"
    }

    /**
     * Returns Boolean if two points are close
     */
    fun isClose(p: Point): Boolean {
        return (areClose(x, p.x) &&
                areClose(y, p.y) &&
                areClose(z, p.z))
    }

    /**
     * Overloading == operator for Points. It uses `are_close` function
     */
    override fun equals(other: Any?): Boolean {
        if (other is Point) {
            return isClose(other)
        }
        return false
    }

    /**
     * Overloading + operator between a point and a vector
     */
    operator fun plus(vec: Vector): Point {
        return Point(x + vec.x, y + vec.y, z + vec.z)
    }

    /**
     * Overloading - operator
     */
    operator fun minus(p: Point): Vector {
        return Vector(abs(x - p.x), abs(y - p.y), abs(z - p.z))
    }

    /**
     * Overloading - operator between a point and a vector
     */
    operator fun minus(v: Vector): Point {
        return Point(x - v.x, y - v.y, z - v.z)
    }

    /**
     * Overloading * operator between a scalar and a point
     */
    operator fun times(a: Float): Point {
        return Point(x * a, y * a, z * a)
    }

    /**
     * Returns a copy of the Point but with components multiplied by -1
     */
    operator fun unaryMinus(): Point {
        return Point(-x, -y, -z)
    }

    /**
     * Converts a point to a vector
     */
    fun toVec(): Vector {
        return Vector(x, y, z)
    }

    /**
     * Overloading of the `get` operator
     */
    operator fun get(i: Int): Float {
        return when (i) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw IllegalArgumentException("i must be 0, 1 or 2")
        }
    }

}


