import kotlin.math.abs
import kotlin.math.withSign

/**
 * Returns True if two Float are close more than epsilon
 */
fun areClose(x: Float, y: Float, epsilon: Float = 1.0E-5F): Boolean {
    return (abs(x - y) < epsilon)
}

/**
 * Returns x/(1+x)
 */
fun clamp(x: Float): Float {
    return x / (x + 1)
}

/**
 * Returns an Orthonormal base from a normal. The normal is the z axis of the new base
 */
fun onbFromZ(normal: Normal): Triple<Vector, Vector, Vector> {
    // withSign returns 1 if the sign is positive and -1 if the sign is negative
    val sign: Float = 1f.withSign(normal.z)
    val a: Float = -1 / (normal.z + sign)
    val b: Float = normal.x * normal.y * a

    val e1 = Vector(1.0f + sign * normal.x * normal.x * a, sign * b, -sign * normal.x)
    val e2 = Vector(b, sign + normal.y * normal.y * a, -normal.y)

    return Triple(e1, e2, Vector(normal.x, normal.y, normal.z))
}

/**
 * Returns an Orthonormal base from a vector. The normal is the z axis of the new base
 */
fun onbFromZ(normal: Vector): Triple<Vector, Vector, Vector> {
    val sign: Float = 1f.withSign(normal.z)
    val a: Float = -1 / (normal.z + sign)
    val b: Float = normal.x * normal.y * a

    val e1 = Vector(1.0f + sign * normal.x * normal.x * a, sign * b, -sign * normal.x)
    val e2 = Vector(b, sign + normal.y * normal.y * a, -normal.y)

    return Triple(e1, e2, Vector(normal.x, normal.y, normal.z))
}

/**
 * evaluates the determinant of a 3x3 matrix
 */
fun determinant3x3(matrix: Array<FloatArray>): Float {
    return matrix[0][0] * matrix[1][1] * matrix[2][2] +
            matrix[0][1] * matrix[1][2] * matrix[2][0] +
            matrix[0][2] * matrix[1][0] * matrix[2][1] -
            matrix[0][2] * matrix[1][1] * matrix[2][0] -
            matrix[0][1] * matrix[1][0] * matrix[2][2] -
            matrix[0][0] * matrix[1][2] * matrix[2][1]
}