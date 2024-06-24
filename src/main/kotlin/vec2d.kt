/**
 * ancilla class for 2d vectors
 */
class Vec2d(val u: Float = 0f, val v: Float = 0f) {
    fun isClose(other: Vec2d, epsilon: Float = 1e-5F): Boolean {
        return areClose(this.u, other.u, epsilon) && areClose(this.v, other.v, epsilon)
    }
}
