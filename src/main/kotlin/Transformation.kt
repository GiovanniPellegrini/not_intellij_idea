open class Transformation(var m: HomMatrix, var invm: HomMatrix) {

    constructor():this(HomMatrix(),HomMatrix())

    /**
     * print transformation matrices
     */
    override fun toString(): String {
        return "Matrix:\n" + m.toString()  + "Inverse:\n" + invm.toString()
    }

    /**
     * Check if the product between matrix and its inverse is the identity 4x4
     */
    fun isConsistent(eps:Float=1e-5f): Boolean {
        val prod = m * invm
        return prod.isClose(HomMatrix(), epsilon = eps)
    }

    /**
     * return Transformation with the matrix and the inverse inverted
     */
    fun inverse(): Transformation {
        return Transformation(invm, m)
    }

    /**
     *verify is two transformation are equals
     */
     fun isClose(other: Transformation,epsilon: Float = 1.0E-5F):Boolean{
        return m.isClose(other.m,epsilon) && invm.isClose(other.invm,epsilon)
     }

    /**
     * override for == operator between two Transformations
     */
    override fun equals(other: Any?):Boolean{
        if (other is Transformation) {
            return this.isClose(other)
        }
        return false
    }

    /**
     * override for * operator between two Transformations
     */
    operator fun times(other: Transformation): Transformation {
        return Transformation(m * other.m, other.invm * invm)
    }

    /**
     * override for * operator between Transformation and Point
     */
    operator fun times(other: Point): Point {
        val newPoint = Point(
            m[0, 0] * other.x + m[0, 1] * other.y + m[0, 2] * other.z + m[0, 3],
            m[1, 0] * other.x + m[1, 1] * other.y + m[1, 2] * other.z + m[1, 3],
            m[2, 0] * other.x + m[2, 1] * other.y + m[2, 2] * other.z + m[2, 3]
        )

        val a = other.x * m[3, 0] + other.y * m[3, 1] + other.z * m[3, 2] + m[3, 3]
        if (a == 1F) return newPoint
        else return Point(newPoint.x / a, newPoint.y / a, newPoint.z / a)
    }

    /**
     * override for * operator between Transformation and Vector
     */
    operator fun times(other: Vector): Vector {
        val newVector = Vector(
            m[0, 0] * other.x + m[0, 1] * other.y + m[0, 2] * other.z ,
            m[1, 0] * other.x + m[1, 1] * other.y + m[1, 2] * other.z ,
            m[2, 0] * other.x + m[2, 1] * other.y + m[2, 2] * other.z
        )

        return newVector
    }

    /**
     * override for * operator between Transformation and Normal
     */
     operator fun times(other:Normal):Normal {
         val newNormal = Normal(
             invm[0, 0] * other.x + invm[0, 1] * other.y + invm[0, 2] * other.z ,
             invm[1, 0] * other.x + invm[1, 1] * other.y + invm[1, 2] * other.z ,
             invm[2, 0] * other.x + invm[2, 1] * other.y + invm[2, 2] * other.z
         )
         return newNormal
     }
}

/**
 * Translation class derived from Transformation
 */
class Translation(): Transformation(HomMatrix(),HomMatrix()) {

    /**
     * constructor of a translation from a translation vector
      */
    constructor(vec: Vector):this(){
         m[0,3]=vec.x
         m[1,3]=vec.y
         m[2,3]=vec.z

         invm[0,3]=-vec.x
         invm[1,3]=-vec.y
         invm[2,3]=-vec.z
     }
}

/**
 * Rotation class derived from Transformation
 */
class Rotation(): Transformation(HomMatrix(),HomMatrix()) {
    /**
     * constructor of a rotation, angular parameter theta must be passed un radians
     */
    constructor(vec: Vector, theta: Float = 0f):this(){
        m = HomMatrix(vec,theta)
        invm = HomMatrix(vec,-theta)
    }
}

/**
 * scale transformation class derived from Transformation
 */
class scalingTransformation(): Transformation(HomMatrix(),HomMatrix()) {
    constructor(sx:Float=1f, sy:Float=1f, sz:Float=1f):this(){
        m = HomMatrix(floatArrayOf(
            sx, 0f, 0f, 0f,
            0f, sy, 0f, 0f,
            0f, 0f, sz, 0f,
            0f, 0f, 0f, 1f,
        ))
        invm = HomMatrix(floatArrayOf(
            1f/sx, 0f, 0f, 0f,
            0f, 1f/sy, 0f, 0f,
            0f, 0f, 1f/sz, 0f,
            0f, 0f, 0f, 1f,
        ))
    }
}

