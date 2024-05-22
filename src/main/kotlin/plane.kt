import kotlin.math.abs
import kotlin.math.floor

class Plane(val transformation: Transformation = Transformation(),
            override val material: Material): Shape{

    /**
     * constructor for xy plane
     */

    override fun rayIntersection(ray: Ray): HitRecord? {
        // compute t
        val invRay = ray.transformation(transformation.inverse())

        if(abs(invRay.dir.z) <1e-5f) return null
        val t=-invRay.origin.z/invRay.dir.z

        if (t<invRay.tMin || t>invRay.tMax) return null

        val normal = if (invRay.dir.z < 0) transformation * Normal(0f, 0f, 1f)
        else transformation * Normal(0f, 0f, -1f)

        val hitPoint = invRay.at(t)
        
        val hit = HitRecord(
            worldPoint = transformation*hitPoint,
            normal = normal,
            surfacePoint = Vec2d(hitPoint.x - floor(hitPoint.x), hitPoint.y - floor(hitPoint.y)),
            t = t,
            ray = ray,
            shape = this
        )
        return hit

    }
}