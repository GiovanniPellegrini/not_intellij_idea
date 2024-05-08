/**
 * data class for ray type
 */
data class Ray(var origin: Point,
               var dir: Vector,
               var tMin: Float = 1e-5f, var tMax: Float = Float.POSITIVE_INFINITY,
               var depth: Int = 0) {

    /**
     * returns if two ray are similar
     */
    fun isClose(other: Ray): Boolean {
        return this.origin.isClose(other.origin) && this.dir.isClose(other.dir)
    }

    /**
     * returns a Point of the ray for a given parameter
     */
    fun at(t: Float): Point{
        return this.origin + this.dir * t
    }

    /**
     * transforms origin and dir type for a given transformation object
     */
    fun transformation(transformation: Transformation): Ray{
        return Ray(transformation*origin,transformation*dir, tMin,tMax,depth)
    }
}