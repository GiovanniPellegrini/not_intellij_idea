/**
 * derived camera class for orthogonal projections
 */
class OrthogonalCamera(private val aspectRatio:Float = 1f, private val transformation: Transformation=Transformation()): Camera {
    /**
     * From 2 coordinates (u,v) returns a ray
     */
    override fun fireRay(u: Float, v: Float): Ray {
        val origin=Point(-1f, (1-2*u)*aspectRatio,2*v-1 )
        val direction=Vector(1f,0f,0f)
        return Ray(origin, direction, tMin=1.0e-5f).transformation(transformation)
    }

}