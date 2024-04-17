import kotlin.math.abs
import kotlin.math.sqrt


/**
 * 3D vector with Float components
 */
data class Vector(var x: Float, var y: Float, var z: Float) {
    /**
     * Constructor of the 0 vector
     */
    constructor() : this(0.0F, 0F, 0.0F)
    /**
     * Print the vector components
     */
    fun print(){
        println("vec(x=${this.x}, y=${this.y}, z=${this.z})")
    }

    /**
     * Boolean to understand if two vector components are close for a given discard
     */
    fun isClose(sampleVec: Vector): Boolean{
        return(are_close(x,sampleVec.x) &&
                are_close(y,sampleVec.y) &&
                are_close(z,sampleVec.z))
    }
    /**
     * Overloading ==operator for Points. It uses the are_close function
     */
    override fun equals(other: Any?): Boolean {
        if (other is Vector) {
            return isClose(other)
        }
        return false
    }


    /**
     * Overloading +operator for the sum of two vectors
     */
    operator fun plus(sampleVec: Vector): Vector {
        return Vector(x+sampleVec.x,y+sampleVec.y,z+sampleVec.z)
    }
    /**
     * Overloading -operator for the difference of two vectors
     */
    operator fun minus(sampleVec: Vector): Vector {
        return Vector(x-sampleVec.x,y-sampleVec.y,z-sampleVec.z)
    }

    /**
     * Overloading * operator for the products between a scalar and a vector
     */
    operator fun times(scalar: Float): Vector{
        return Vector(x*scalar,y*scalar,z*scalar)
    }

    /**
     * Returns a copy of the vector but with components multiplied by -1
     */
    operator fun unaryMinus(): Vector {
        return Vector(-x,-y,-z)
    }

    /**
     * Overloading *operator for the scalar products between two vectors
     */
    operator fun times(other: Vector): Float {
        return x*other.x+y*other.y+z*other.z
    }

    /**
     * Returns the vector products between two vectors
     */
    fun xTimes(other: Vector): Vector{
        return Vector(y*other.z-z*other.y,
                      z*other.x-x*other.z,
                      x*other.y-y*other.x)
    }

    /**
     * Returns the squared norm of the Vector
     */
    fun sqNorm(): Float {
        return this*this
    }

    /**
     * Returns the norm of the Vector
     */
    fun norm(): Float {
        return sqrt(sqNorm())
    }

    /**
     * Method for normalizing the Vector
     */
    fun normalize() {
        x /= norm()
        y /= norm()
        z /= norm()
    }

    /**
     * Method to convert to a normal
     */
    fun toNormal(): Normal{
        return Normal(x,y,z)
    }

}

