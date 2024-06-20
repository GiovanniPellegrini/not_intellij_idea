import kotlin.math.abs
import kotlin.math.floor

/**
 * Plane class: Derived from Shape, represents a plane in the XY plane
 */
class Plane(
    override val transformation: Transformation = Transformation(),
    override val material: Material = Material()
) : Shape {


    /**
     * Returns Boolean if a point is internal to the plane
     */
    override fun pointInternal(point: Point): Boolean {
        val point1 = transformation.inverse() * point
        return point1.z < 1e-5f

    }

    /**
     * Returns the closest ray intersection with plane
     */
    override fun rayIntersection(ray: Ray): HitRecord? {
        val invRay = ray.transformation(transformation.inverse())

        if (abs(invRay.dir.z) < 1e-5f) return null
        val t = -invRay.origin.z / invRay.dir.z
        if (t < invRay.tMin || t > invRay.tMax) return null
        val normal = if (invRay.dir.z < 0) Normal(0f, 0f, 1f)
        else Normal(0f, 0f, -1f)

        val hitPoint = invRay.at(t)
        val hit = HitRecord(
            worldPoint = transformation * hitPoint,
            normal = transformation * normal,
            surfacePoint = Vec2d(hitPoint.x - floor(hitPoint.x), hitPoint.y - floor(hitPoint.y)),
            t = t,
            ray = ray,
            shape = this
        )
        return hit
    }

    /**
     * Returns all the ray intersections with plane
     */
    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        val hits = ArrayList<HitRecord>()
        if (this.rayIntersection(ray) == null) return null
        else {
            hits.add(this.rayIntersection(ray)!!)
            return hits
        }
    }

    override fun quickRayIntersection(ray: Ray): Boolean {
        val invRay = ray.transformation(transformation.inverse())
        if (abs(invRay.dir.z) < 1e-5f) return false

        val t = -invRay.origin.z / invRay.dir.z
        return invRay.tMin < t && t < invRay.tMax
    }
}