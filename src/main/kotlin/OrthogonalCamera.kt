class OrthogonalCamera( private val aspectRatio:Float, private val transformation: Transformation=Transformation()): Camera {
    override fun fireRay(u: Float, v: Float): Ray {
        val origin=Point(-1f, (1-2*u)*aspectRatio,2*v-1 )
        val direction=Vector(1f,0f,0f)
        return Ray(origin, direction).transformation(transformation) // da implementare
    }

}