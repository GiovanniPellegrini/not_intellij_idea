import kotlin.math.PI

/**
 * Transformation class: Represents a generic transformation in 3D space
 *
 * @param m: Homogeneous matrix representing the transformation
 * @param invm: Inverse of the homogeneous matrix
 *
 * @default constructor: identity transformation
 */

open class Transformation(var m: HomMatrix = HomMatrix(), var invm: HomMatrix = HomMatrix()) {
    /**
     * Returns transformation matrices as a string
     */
    override fun toString(): String {
        return "Matrix:\n" + m.toString() + "Inverse:\n" + invm.toString()
    }

    /**
     * Returns Boolean if the product between matrix and its inverse is the identity 4x4
     */
    fun isConsistent(eps: Float = 1e-5f): Boolean {
        val prod = m * invm
        return prod.isClose(HomMatrix(), epsilon = eps)
    }

    /**
     * Returns inverse Transformation
     */
    fun inverse(): Transformation {
        return Transformation(invm, m)
    }

    /**
     *Returns Boolean if two transformation are equals
     */
    fun isClose(other: Transformation, epsilon: Float = 1.0E-5F): Boolean {
        return m.isClose(other.m, epsilon) && invm.isClose(other.invm, epsilon)
    }

    /**
     * Overloading == operator between two Transformations
     */
    override fun equals(other: Any?): Boolean {
        if (other is Transformation) {
            return this.isClose(other)
        }
        return false
    }

    /**
     * Overloading * operator between two Transformations
     */
    operator fun times(other: Transformation): Transformation {
        return Transformation(m * other.m, other.invm * invm)
    }

    /**
     * Overloading * operator between Transformation and Point
     */
    operator fun times(other: Point): Point {
        val newPoint = Point(
            m[0, 0] * other.x + m[0, 1] * other.y + m[0, 2] * other.z + m[0, 3],
            m[1, 0] * other.x + m[1, 1] * other.y + m[1, 2] * other.z + m[1, 3],
            m[2, 0] * other.x + m[2, 1] * other.y + m[2, 2] * other.z + m[2, 3]
        )

        val a = other.x * m[3, 0] + other.y * m[3, 1] + other.z * m[3, 2] + m[3, 3]
        return if (a == 1F) {
            newPoint
        } else Point(newPoint.x / a, newPoint.y / a, newPoint.z / a)
    }

    /**
     * Overloading * operator between Transformation and Vector
     */
    operator fun times(other: Vector): Vector {
        val newVector = Vector(
            m[0, 0] * other.x + m[0, 1] * other.y + m[0, 2] * other.z,
            m[1, 0] * other.x + m[1, 1] * other.y + m[1, 2] * other.z,
            m[2, 0] * other.x + m[2, 1] * other.y + m[2, 2] * other.z
        )

        return newVector
    }

    /**
     * Overloading * operator between Transformation and Normal
     */
    operator fun times(other: Normal): Normal {
        val newNormal = Normal(
            invm[0, 0] * other.x + invm[1, 0] * other.y + invm[2, 0] * other.z,
            invm[0, 1] * other.x + invm[1, 1] * other.y + invm[2, 1] * other.z,
            invm[0, 2] * other.x + invm[1, 2] * other.y + invm[2, 2] * other.z
        )
        return newNormal
    }

    /**
     * Overloading * operator between transformation and Hitpoint
     */
    operator fun times(other: HitRecord): HitRecord {
        val newHitRecord = HitRecord(
            worldPoint = this * other.worldPoint,
            normal = this * other.normal,
            surfacePoint = other.surfacePoint,
            t = other.t,
            ray = other.ray,
            shape = other.shape
        )
        return newHitRecord
    }

    /**
     * Overloading * operator for sphere
     */
    operator fun times(other: Sphere): Sphere {
        return Sphere(this * other.transformation)
    }
}

/**
 * Translation class: Derived class of Transformation, represents a translation in 3D space
 *
 * @default constructor: identity transformation
 *
 * @param vec: Vector representing the translation vector
 */
class Translation() : Transformation(HomMatrix(), HomMatrix()) {

    /**
     * constructor of a translation from a translation vector
     */
    constructor(vec: Vector) : this() {
        m[0, 3] = vec.x
        m[1, 3] = vec.y
        m[2, 3] = vec.z

        invm[0, 3] = -vec.x
        invm[1, 3] = -vec.y
        invm[2, 3] = -vec.z
    }
}

/**
 * Rotation class: Derived class of Transformation, represents a rotation in 3D space
 *
 * @default constructor: identity transformation
 *
 * @param vec: Vector representing the axis of rotation
 * @param theta: Angle of rotation in degrees
 */
class Rotation() : Transformation(HomMatrix(), HomMatrix()) {

    constructor(vec: Vector, theta: Float = 0f) : this() {
        val thetaInRadians = theta * PI.toFloat() / 180f
        m = HomMatrix(vec, thetaInRadians)
        invm = HomMatrix(vec, -thetaInRadians)
    }
}

/**
 * Scaling Transformation class: Derived class of Transformation, represents a scaling transformation in 3D space
 *
 * @default constructor: identity transformation
 *
 * @param s: Vector representing the scaling factors in x, y, z directions
 */
class scalingTransformation() : Transformation(HomMatrix(), HomMatrix()) {

    constructor(s: Vector = Vector(1f, 1f, 1f)) : this() {
        m = HomMatrix(
            floatArrayOf(
                s.x, 0f, 0f, 0f,
                0f, s.y, 0f, 0f,
                0f, 0f, s.z, 0f,
                0f, 0f, 0f, 1f,
            )
        )
        invm = HomMatrix(
            floatArrayOf(
                1f / s.x, 0f, 0f, 0f,
                0f, 1f / s.y, 0f, 0f,
                0f, 0f, 1f / s.z, 0f,
                0f, 0f, 0f, 1f,
            )
        )
    }
}

