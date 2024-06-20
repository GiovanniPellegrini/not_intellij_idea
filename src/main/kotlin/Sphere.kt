import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * Sphere class: Derived from Shape, represents a unit sphere centered at the origin.
 *
 * @param transformation: transformation applied to the sphere
 * @param material: material of the sphere
 */

class Sphere(
    override val transformation: Transformation = Transformation(),
    override val material: Material = Material()
) : Shape {

    /**
     * Returns Boolean if a point is internal to the sphere
     */
    override fun pointInternal(point: Point): Boolean {
        val point1 = transformation.inverse() * point
        if (point1.toVec().sqNorm() <= 1f) return true
        else return false
    }

    /**
     * Returns the closest ray intersection with sphere using equation t^2*d^2+(2*o*d)*t+o^2-1=0
     */
    override fun rayIntersection(ray: Ray): HitRecord? {
        val invRay = ray.transformation(transformation.inverse())
        val origin = invRay.origin.toVec()
        val d2 = invRay.dir.sqNorm()
        val b = origin.sqNorm()

        val delta = (origin * invRay.dir) * (origin * invRay.dir) - d2 * (b - 1f)

        if (delta <= 0) return null
        //compute solutions
        val deltaSqr = sqrt(delta)
        val t1 = (-origin * invRay.dir - deltaSqr) / d2
        val t2 = (-origin * invRay.dir + deltaSqr) / d2

        val firstHitT: Float = if (invRay.tMin < t1 && t1 < invRay.tMax) {
            t1
        } else if (invRay.tMin < t2 && t2 < invRay.tMax) {
            t2
        } else {
            return null
        }

        val hitPoint = invRay.at(firstHitT)
        return HitRecord(
            worldPoint = this.transformation * hitPoint,
            normal = this.transformation * sphereNormal(hitPoint, invRay.dir),
            surfacePoint = spherePointToUV(hitPoint),
            t = firstHitT,
            ray = ray,
            shape = this
        )
    }

    /**
     * Returns all the ray intersections with sphere using equation t^2*d^2+(2*o*d)*t+o^2-1=0
     */
    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        val hits = ArrayList<HitRecord>()
        val invRay = ray.transformation(transformation.inverse())
        val origin = invRay.origin.toVec()
        val d2 = invRay.dir.sqNorm()
        val b = origin.sqNorm()
        val delta = (origin * invRay.dir) * (origin * invRay.dir) - d2 * (b - 1f)

        if (delta <= 0) return null
        val deltaSqr = sqrt(delta)
        val t1 = (-origin * invRay.dir - deltaSqr) / d2
        val t2 = (-origin * invRay.dir + deltaSqr) / d2

        if (invRay.tMin < t1 && t1 < invRay.tMax) {
            val hitPoint1 = invRay.at(t1)
            hits.add(
                HitRecord(
                    worldPoint = this.transformation * hitPoint1,
                    normal = this.transformation * sphereNormal(hitPoint1, invRay.dir),
                    surfacePoint = spherePointToUV(hitPoint1),
                    t = t1,
                    ray = ray,
                    shape = this
                )
            )
        }
        if (invRay.tMin < t2 && t2 < invRay.tMax) {
            val hitPoint2 = invRay.at(t2)
            hits.add(
                HitRecord(
                    worldPoint = this.transformation * hitPoint2,
                    normal = this.transformation * sphereNormal(hitPoint2, invRay.dir),
                    surfacePoint = spherePointToUV(hitPoint2),
                    t = t2,
                    ray = ray,
                    shape = this
                )
            )
        }
        return if (hits.isEmpty()) null
        else hits
    }


    /**
     * Returns the normal of the sphere at a given point with respect to the direction of the incident ray
     */
    private fun sphereNormal(point: Point, rayDir: Vector): Normal {
        val result = Normal(point.x, point.y, point.z)
        return if (point.toVec() * rayDir < 0f) {
            result
        } else {
            -result
        }
    }

    /**
     * Returns the UV coordinates of a point on the sphere
     */
    private fun spherePointToUV(point: Point): Vec2d {
        val u: Float = atan2(point.y, point.x) / (2f * PI.toFloat())
        return if (u >= 0) {
            Vec2d(u = u, v = acos(point.z) / PI.toFloat())
        } else {
            Vec2d(u = u + 1f, v = acos(point.z) / PI.toFloat())
        }
    }
}