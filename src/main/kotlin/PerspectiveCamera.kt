/**
 * Perspective camera class: Derived camera class, represents a camera with perspective projection
 *
 * @param distance: distance from the camera to the image plane
 * @param aspectRatio: Aspect ratio of the camera (width/height)
 * @param transformation: transformation applied to the camera
 */

class PerspectiveCamera(
    private val distance: Float = 1F,
    private val aspectRatio: Float = 1F,
    private val transformation: Transformation = Transformation()
) : Camera {

    /**
     * Overridden function that generates a ray for a given pixel coordinates
     */
    override fun fireRay(u: Float, v: Float): Ray {
        val origin = Point(-distance, 0F, 0F)
        val direction = Vector(distance, (1f - 2f * u) * aspectRatio, 2f * v - 1f)
        return Ray(origin, direction).transformation(transformation)
    }

}