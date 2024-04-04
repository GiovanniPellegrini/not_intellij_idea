data class Point (var x:Float, var y:Float, var z:Float) {
    /**
     * Null constructor
     */
    constructor() : this(0.0F,0.0F,0.0F)
    /**
     * Converting the object to String
     */
    override fun toString():String{
        return "<x:$x, y=$y, z=$z"
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

    operator fun plus(vec: Vector): Point {
        return Point(x+vec.x,y+vec.y,z+vec.z)
    }




}


