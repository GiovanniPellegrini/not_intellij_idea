import kotlin.math.floor

class plane(val transformation: Transformation = Transformation(),
            val origin: Point = Point(),
            val vector: Vector = Vector()): Shape{

    /**
     * constructor for xy plane
     */
    constructor(transformation: Transformation): this(transformation,
                                                      Point(0f,0f,0f),
                                                      Vector(0f,0f,1f))

    override fun rayIntersection(ray: Ray): HitRecord? {
        // compute t
        val invRay = ray.transformation(transformation.inverse())
        val origin = invRay.origin.toVec()
        var t: Float? = null

        // control if there is the intersection.
        if (invRay.dir.z != 0f) {
            t = -(origin.z / invRay.dir.z)
        } else return null
        if (t < invRay.tMin || t > invRay.tMax) return null

        //
        val normal = if (invRay.dir.z < 0) transformation * Normal(0f, 0f, 1f)
        else transformation * Normal(0f, 0f, -1f)

        val hitPoint = invRay.at(t)

        val hit = HitRecord(
            worldPoint = hitPoint,
            normal = normal,
            surfacePoint = Vec2d(hitPoint.x - floor(hitPoint.x), hitPoint.y - floor(hitPoint.y)),
            t = t,
            ray = ray
        )
        return hit
    }
}