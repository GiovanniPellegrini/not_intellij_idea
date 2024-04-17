class PerspectiveCamera(private val distance:Float=1F, private val aspectRatio: Float =1F, private val transformation: Transformation=Transformation()):Camera {
    override fun fireRay(u:Float, v:Float):Ray{
        val origin=Point(-distance,0F,0F)
        val direction=Vector(distance, (1-2*u)*aspectRatio, 2*v-1)
        return Ray(origin, direction).transformation(transformation)
    }
}