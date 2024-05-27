import kotlin.math.sqrt

data class Normal(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f) {
    /**
     * print Vector components
     */
    fun print(){
        println("Normal(x=${this.x}, y=${this.y}, z=${this.z}")
    }

    /**
     * Boolean to understand if two Normal components are close for a given discard
     */
    fun isClose(sampleNorm: Normal): Boolean{
        return(areClose(x,sampleNorm.x) &&
                areClose(y,sampleNorm.y) &&
                areClose(z,sampleNorm.z))
    }

    /**
     * Returns a copy of the vector but with components multiplied by -1
     */
    operator fun unaryMinus(): Normal {
        return Normal(-x,-y,-z)
    }

    /**
     * Overloading * operator for the products between a scalar and a vector
     */
    operator fun times(scalar: Float): Normal{
        return Normal(x*scalar,y*scalar,z*scalar)
    }

    /**
     * Overloading *operator for the scalar products between two normals
     */
    operator fun times(other: Normal): Float {
        return x*other.x+y*other.y+z*other.z
    }

    /**
     * Overloading *operator for the scalar products between a normal and a vector
     */
    operator fun times(other: Vector): Float {
        return x*other.x+y*other.y+z*other.z
    }

    /**
     * Returns the vector products between two normals
     */
    fun xTimes(other: Normal): Normal{
        return Normal(y*other.z-z*other.y,
            z*other.x-x*other.z,
            x*other.y-y*other.x)
    }
    /**
     * Returns the vector products between a normal and a vector
     */
    fun xTimes(other: Vector): Normal{
        return Normal(y*other.z-z*other.y,
            z*other.x-x*other.z,
            x*other.y-y*other.x)
    }

    /**
     * Returns the squared norm of the Normal
     */
    fun sqNorm(): Float {
        return this*this
    }

    /**
     * Returns the norm of the Normal
     */
    fun norm(): Float {
        return sqrt(sqNorm())
    }

    /**
     * Method for normalizing the Vector
     */
    fun normalize (): Normal {
        val norm = norm()
        x /= norm
        y /= norm
        z /= norm
        return Normal(x,y,z)
    }

    /**
     * Method to convert a normal into a vector
     */
    fun toVector(): Vector{
        return Vector(x,y,z)
    }

    /**
     * scalar product between two normalized normals
     */
    fun normalizedDot(other: Normal): Float {
        val n1 = Normal(x,y,z)
        val n2 = Normal(other.x,other.y,other.z)
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