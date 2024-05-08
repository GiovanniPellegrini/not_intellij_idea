/**
 * ancilla class for 2d vectors
 */
class Vec2d(val u: Float = 0f, val v: Float = 0f){
    fun isClose(other: Vec2d, epsilon: Float = 1e-5F): Boolean {
        return are_close(this.u, other.u, epsilon) && are_close(this.v, other.v, epsilon)
    }
}
