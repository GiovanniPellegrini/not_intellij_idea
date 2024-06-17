/**
 * Ray class: represents a light ray in 3D space
 * r(t) = O + t*d
 *
 * @param origin: Origin point of the ray
 * @param dir: Direction vector of the ray
 * @param tMin: Minimum value of the parameter t
 * @param tMax: Maximum value of the parameter t
 * @param depth: Number of reflections of the ray
 */
data class Ray(
    var origin: Point,
    var dir: Vector,
    var tMin: Float = 1e-5f,
    var tMax: Float = Float.POSITIVE_INFINITY,
    var depth: Int = 0
) {

    /**
     * Returns Boolean if two rays are close
     */
    fun isClose(other: Ray): Boolean {
        return this.origin.isClose(other.origin) && this.dir.isClose(other.dir)
    }

    /**
     * Returns a point of the ray for a given parameter t
     */
    fun at(t: Float): Point {
        return this.origin + this.dir * t
    }

    /**
     * Returns a new ray after applying a transformation
     */
    fun transformation(transformation: Transformation): Ray {
        return Ray(transformation * origin, transformation * dir, tMin, tMax, depth)
    }
}