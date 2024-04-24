/**
 * ancilla class for 2d vectors
 */
class Vec2d(val u: Float, val v: Float){
    constructor(): this(0F, 0F)

    fun isClose(other: Vec2d, epsilon: Float = 1e-5F): Boolean {
        return are_close(this.u, other.u, epsilon) && are_close(this.v, other.v, epsilon)
    }
}
