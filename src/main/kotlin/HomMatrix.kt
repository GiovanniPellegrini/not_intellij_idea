import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * HomMatrix Class: Represents homogeneous 4x4 matrix
 *
 * @param elements: array of 16 floats
 * @default constructor: identity matrix
 */
data class HomMatrix(var elements: FloatArray) {
    init {
        require(elements.size == 16) { "A homogeneous matrix must be 4×4" }
    }

    /**
     * Constructor of the identity matrix
     */
    constructor() : this(
        floatArrayOf(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )

    /**
     * Constructor of an array with all elements equal to the argument
     */
    constructor(a: Float) : this() {
        for (i in elements.indices) {
            elements[i] = a
        }
    }

    /**
     * Constructor of a rotation matrix
     *
     * @param v: Vector representing the axis of rotation
     * @param theta: Angle of rotation in degrees
     */
    constructor(v: Vector, theta: Float = 0f) : this() {
        v.normalize()
        elements[0] = cos(theta) + v.x.pow(2) * (1 - cos(theta))
        elements[1] = v.x * v.y * (1 - cos(theta)) - v.z * sin(theta)
        elements[2] = v.x * v.z * (1 - cos(theta)) + v.y * sin(theta)
        elements[3] = 0f
        elements[4] = v.y * v.x * (1 - cos(theta)) + v.z * sin(theta)
        elements[5] = cos(theta) + (v.y.pow(2) * (1 - cos(theta)))
        elements[6] = v.y * v.z * (1 - cos(theta)) - v.x * sin(theta)
        elements[7] = 0f
        elements[8] = v.z * v.x * (1 - cos(theta)) - v.y * sin(theta)
        elements[9] = v.z * v.y * (1 - cos(theta)) + v.x * sin(theta)
        elements[10] = cos(theta) + (v.z.pow(2) * (1 - cos(theta)))
        elements[11] = 0f
        elements[12] = 0f
        elements[13] = 0f
        elements[14] = 0f
        elements[15] = 1f
    }

    /**
     * Returns matrix components as a string
     */
    override fun toString(): String {

        val mat = "${this[0, 0]} ${this[0, 1]} ${this[0, 2]} ${this[0, 3]}\n" +
                "${this[1, 0]} ${this[1, 1]} ${this[1, 2]} ${this[1, 3]}\n" +
                "${this[2, 0]} ${this[2, 1]} ${this[2, 2]} ${this[2, 3]}\n" +
                "${this[3, 0]} ${this[3, 1]} ${this[3, 2]} ${this[3, 3]}\n"

        return mat
    }

    /**
     * Returns boolean value if two HomMatrices are close
     */
    fun isClose(other: HomMatrix, epsilon: Float = 1.0E-5F): Boolean {
        for (i in 0 until 16) {
            if (!areClose(this[i], other[i], epsilon)) {
                return false
            }
        }
        return true
    }

    /**
     * Overloading of get operator with one index
     */
    operator fun get(i: Int): Float {
        return elements[i]
    }

    /**
     * Overloading of get operator with two indexes
     */
    operator fun get(x: Int, y: Int): Float {
        return elements[x * 4 + y]
    }

    /**
     * Overloading of set operator with one index
     */
    operator fun set(i: Int, b: Float) {
        elements[i] = b
    }

    /**
     * Overloading of set operator with two indexes
     */
    operator fun set(x: Int, y: Int, b: Float) {
        elements[x * 4 + y] = b
    }

    /**
     * Row by column product between two HomMatrices
     */
    operator fun times(other: HomMatrix): HomMatrix {
        val result = HomMatrix(0f)
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                for (k in 0 until 4) {
                    result[i, j] += this[i, k] * other[k, j]
                }
            }
        }
        return result
    }

}
