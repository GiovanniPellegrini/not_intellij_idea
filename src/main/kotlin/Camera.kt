/**
 * Camera interface: Represents the object that generates the rays for backward tracing
 *
 * @property fireRay: Abstract function that generates a ray for a given pixel coordinates
 */
interface Camera {
    fun fireRay(u: Float, v: Float): Ray
}