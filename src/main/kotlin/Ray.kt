
data class Ray(var origin: Point,
               var dir: Vector,
               var tMin: Float = 1e-5f, var tMax: Float = Float.POSITIVE_INFINITY,
               var depth: Int = 0) {

    fun isClose(other: Ray, epsilon: Float = 1e-5f): Boolean {
        return this.origin.isClose(other.origin) && this.dir.isClose(other.dir)
    }

    fun at(t: Float): Point{
        return this.origin + this.dir * t
    }

    fun transformation(transformation: Transformation):Ray{
        return Ray(transformation*origin,transformation*dir, tMin,tMax,depth)
    }
}