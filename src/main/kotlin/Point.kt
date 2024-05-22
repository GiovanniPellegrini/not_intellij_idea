import kotlin.math.abs

data class Point (var x:Float, var y:Float, var z:Float) {
    /**
     * Null constructor
     */
    constructor() : this(0.0F,0.0F,0.0F)
    /**
     * Converting the object to String
     */
    override fun toString():String{
        return "<x:$x, y=$y, z=$z>"
    }

    fun isClose(p: Point): Boolean{
        return(are_close(x,p.x) &&
                are_close(y,p.y) &&
                are_close(z,p.z))
    }
    /**
     * Overloading ==operator for Points. It uses the are_close function
     */
    override fun equals(other: Any?): Boolean {
        if (other is Point) {
            return isClose(other)
        }
        return false
    }

    /**
     * sum operator overloading between a Point and a Vector (returns a Point)
     */
    operator fun plus(vec: Vector): Point {
        return Point(x+vec.x,y+vec.y,z+vec.z)
    }

    /**
     * minus operator overloading between two Points (returns a Vector)
     */
    operator fun minus(p: Point): Vector {
        return Vector(abs(x-p.x),abs(y-p.y),abs(z-p.z))
    }

    /**
     * minus operator overloading between a Point and a Vector (returns a Point)
     */
    operator fun minus(v: Vector): Point {
        return Point(x-v.x,y-v.y,z-v.z)
    }

    /**
     * Product operator overloading between a scalar and a point
     */
    operator fun times(a: Float): Point {
        return Point(x*a,y*a,z*a)
    }

    /**
     * Returns a copy of the Point but with components multiplied by -1
     */
    operator fun unaryMinus(): Point {
        return Point(-x,-y,-z)
    }

    /**
     * converts a Point to a Vector with same components
     */
    fun toVec(): Vector {
        return Vector(x,y,z)
    }

    operator fun get(i: Int): Float {
        return when(i){
            0 -> x
            1 -> y
            2 -> z
            else -> throw IllegalArgumentException("i must be 0, 1 or 2")
        }

    }

}


