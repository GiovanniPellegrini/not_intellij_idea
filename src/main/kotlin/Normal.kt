import kotlin.math.sqrt

/**
 * Normal Class: Represents a 3D normal with Float components
 *
 * @param x: x component
 * @param y: y component
 * @param z: z component
 *
 * @Default Constructor: x=0, y=0, z=0
 */
data class Normal(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f) {

    /**
     * Returns vector components as a string
     */
    override fun toString(): String {
        return "Normal:<x=$x,y=$y,z=$z>"
    }

    /**
     * Returns Boolean if two normals are close
     */
    fun isClose(sampleNorm: Normal): Boolean {
        return (areClose(x, sampleNorm.x) &&
                areClose(y, sampleNorm.y) &&
                areClose(z, sampleNorm.z))
    }

    /**
     * Returns a copy of the normal with components multiplied by -1
     */
    operator fun unaryMinus(): Normal {
        return Normal(-x, -y, -z)
    }

    /**
     * Overloading * operator between a scalar and a normal
     */
    operator fun times(scalar: Float): Normal {
        return Normal(x * scalar, y * scalar, z * scalar)
    }

    /**
     * Overloading * operator for scalar products between two normals
     */
    operator fun times(other: Normal): Float {
        return x * other.x + y * other.y + z * other.z
    }

    /**
     * Overloading * operator for the scalar products between a normal and a vector
     */
    operator fun times(other: Vector): Float {
        return x * other.x + y * other.y + z * other.z
    }

    /**
     * Returns the vector products between two normals
     */
    fun xTimes(other: Normal): Normal {
        return Normal(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )
    }

    /**
     * Returns the vector products between a normal and a vector
     */
    fun xTimes(other: Vector): Normal {
        return Normal(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )
    }

    /**
     * Returns the squared norm of the Normal
     */
    fun sqNorm(): Float {
        return this * this
    }

    /**
     * Returns the norm of the Normal
     */
    fun norm(): Float {
        return sqrt(sqNorm())
    }

    /**
     * Applies normalization to the normal
     */
    fun normalize(): Normal {
        val norm = norm()
        x /= norm
        y /= norm
        z /= norm
        return Normal(x, y, z)
    }

    /**
     * Converts to a vector
     */
    fun toVector(): Vector {
        return Vector(x, y, z)
    }

    /**
     * Overloading of the `get` operator
     */
    operator fun get(i: Any): Float {
        return when (i) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw IllegalArgumentException("i must be 0, 1 or 2")
        }
    }

    /**
     * scalar product between two normalized normals
     */
    fun normalizedDot(other: Normal): Float {
        val n1 = Normal(x, y, z)
        val n2 = Normal(other.x, other.y, other.z)
        n1.normalize()
        n2.normalize()
        return n1 * n2
    }

    /**
     * scalar product between a normalized vector and normalized normal
     */
    fun normalizedDot(other: Vector): Float {
        val n1 = Normal(x, y, z)
        val n2 = Normal(other.x, other.y, other.z)
        n1.normalize()
        n2.normalize()
        return n1 * n2
    }


}