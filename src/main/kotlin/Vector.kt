import kotlin.math.sqrt

/**
 * Vector Class: Represents a 3D vector with Float components
 *
 * @param x: x component
 * @param y: y component
 * @param z: z component
 *
 * @default Constructor: x=0, y=0, z=0
 */
data class Vector(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f) {

    /**
     * Returns vector components as a string
     */
    override fun toString(): String {
        return "Vector:<x=$x,y=$y,z=$z>"
    }

    /**
     * Returns Boolean if two vectors are close
     */
    fun isClose(sampleVec: Vector): Boolean {
        return (areClose(x, sampleVec.x) &&
                areClose(y, sampleVec.y) &&
                areClose(z, sampleVec.z))
    }

    /**
     * Overloading == operator for vectors. It uses `are_close` function
     */
    override fun equals(other: Any?): Boolean {
        if (other is Vector) {
            return isClose(other)
        }
        return false
    }


    /**
     * Overloading + operator
     */
    operator fun plus(sampleVec: Vector): Vector {
        return Vector(x + sampleVec.x, y + sampleVec.y, z + sampleVec.z)
    }

    /**
     * Overloading - operator
     */
    operator fun minus(sampleVec: Vector): Vector {
        return Vector(x - sampleVec.x, y - sampleVec.y, z - sampleVec.z)
    }

    /**
     * Overloading * operator between a scalar and a vector
     */
    operator fun times(scalar: Float): Vector {
        return Vector(x * scalar, y * scalar, z * scalar)
    }

    /**
     * Returns a copy of the vector with components multiplied by -1
     */
    operator fun unaryMinus(): Vector {
        return Vector(-x, -y, -z)
    }

    /**
     * Overloading * operator for the scalar product between two vectors
     */
    operator fun times(other: Vector): Float {
        return x * other.x + y * other.y + z * other.z
    }

    /**
     * Returns the vector products between two vectors
     */
    fun xTimes(other: Vector): Vector {
        return Vector(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )
    }

    /**
     * Returns the squared norm of the vector
     */
    fun sqNorm(): Float {
        return this * this
    }

    /**
     * Returns the norm of the vector
     */
    fun norm(): Float {
        return sqrt(sqNorm())
    }

    /**
     * Applies normalization to the vector
     */
    fun normalize() {
        val norm = this.norm()
        x /= norm
        y /= norm
        z /= norm
    }

    /**
     * Converts to a normal
     */
    fun toNormal(): Normal {
        return Normal(x, y, z)
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
     * scalar product between two normalized vectors
     */
    fun normalizedDot(other: Vector): Float {
        val v1 = Vector(this.x,this.y,this.z)
        val v2 = Vector(other.x,other.y,other.z)
        v1.normalize()
        v2.normalize()
        return v1*v2
    }

    /**
     * scalar product between normalized normal and vector
     */

    fun normalizedDot(other: Normal): Float {
        val v1 = Vector(this.x, this.y, this.z)
        val v2 = Vector(other.x, other.y, other.z)
        v1.normalize()
        v2.normalize()
        return v1 * v2
    }

}

