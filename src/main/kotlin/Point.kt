/**
 * Class of a 3D point with xyz coordinates
 */
data class Point(var x: Float, var y: Float, var z: Float) {
    /**
     * Constructor of the origin point
     */
    constructor() : this(0.0F, 0F, 0.0F)

    /**
     * Print the vector components
     */
    fun print(){
        println("Point(x=${this.x}, y=${this.y}, z=${this.z}")
    }

    fun isClose(other: Point): Boolean {
        return (are_close(x, other.x) &&
                are_close(y, other.y) &&
                are_close(z, other.z))
    }

    operator fun plus(other: Vector): Point {
        return Point(x+other.x,y+other.y,z+other.z)
    }
}