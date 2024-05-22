import kotlin.math.abs
import kotlin.math.floor

class Plane(val transformation: Transformation = Transformation(),
            override val material: Material=Material()): Shape{


    /**
     * check if a point belongs to the plane
     */
    override fun pointInternal(point: Point): Boolean {
        val point1=transformation.inverse()*point
        if(point1.z<1e-5f) return true
        else return false
    }

    override fun rayIntersection(ray: Ray): HitRecord? {
        // compute t
        val invRay = ray.transformation(transformation.inverse())

        if(abs(invRay.dir.z) <1e-5f) return null
        val t=-invRay.origin.z/invRay.dir.z

        if (t<invRay.tMin || t>invRay.tMax) return null

        val normal = if (invRay.dir.z < 0)  Normal(0f, 0f, 1f)
        else  Normal(0f, 0f, -1f)

        val hitPoint = invRay.at(t)
        
        val hit = HitRecord(
            worldPoint = transformation*hitPoint,
            normal =transformation* normal,
            surfacePoint = Vec2d(hitPoint.x - floor(hitPoint.x), hitPoint.y - floor(hitPoint.y)),
            t = t,
            ray = ray,
            shape = this
        )
        return hit
    }
    /**
     *evaluates if a ray intersect the plane and returns all the intersection from the point of view
     */
    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        val hits= ArrayList<HitRecord>()
        if(this.rayIntersection(ray)==null) return null
        else {
            hits.add(this.rayIntersection(ray)!!)
            return hits
        }
    }
}