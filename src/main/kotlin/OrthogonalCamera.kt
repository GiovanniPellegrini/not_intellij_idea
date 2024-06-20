/**
 * Orthogonal camera class: Derived class of Camera, represents a camera with orthogonal projection
 *
 * @param aspectRatio: Aspect ratio of the camera (width/height)
 * @param transformation: Transformation applied to the camera
 */
class OrthogonalCamera(
    private val aspectRatio: Float = 1f,
    private val transformation: Transformation = Transformation()
) : Camera {

    /**
     * Overridden function that generates a ray for a given pixel coordinates
     */
    override fun fireRay(u: Float, v: Float): Ray {
        val origin = Point(-1f, (1 - 2 * u) * aspectRatio, 2 * v - 1)
        val direction = Vector(1f, 0f, 0f)
        return Ray(origin, direction, tMin = 1.0e-5f).transformation(transformation)
    }
}