/**
 * derived camera class for perspective projections
 */
class PerspectiveCamera(private val distance:Float=1F, private val aspectRatio: Float =1F, private val transformation: Transformation=Transformation()):Camera {

    /**
     * From 2 coordinates (u,v) returns a ray
     */
    override fun fireRay(u:Float, v:Float):Ray{
        val origin=Point(-distance,0F,0F)
        val direction=Vector(distance, (1f-2f*u)*aspectRatio, 2f*v-1f)
        return Ray(origin, direction).transformation(transformation)
    }
}