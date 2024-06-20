import kotlin.math.*

/**
 * Cone shape with a circumference with radius 1 base centered at (0,0,0) and vertex at (0,0,0) is developed
 */

class Cone(val transformation: Transformation = Transformation(), override val material: Material = Material()) :
    Shape {
    override fun pointInternal(point: Point): Boolean {
        val invPoint = transformation.inverse() * point
        return if (invPoint.z in 0f..1f && invPoint.x.pow(2) + invPoint.y.pow(2) - (invPoint.z - 1f).pow(2) <= 0) true
        else false
    }

    /**
     * evaluates if a ray intersect a cone and return the closest intersection.
     * To understand the math of the code visit  https://lousodrome.net/blog/light/2017/01/03/intersection-of-a-ray-and-a-cone/
     */
    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        val hits = ArrayList<HitRecord>()

        val invRay = ray.transformation(transformation.inverse())
        val origin = invRay.origin.toVec()
        val direction = invRay.dir
        direction.normalize()

        val C = Vector(0f, 0f, 1f)
        val V = Vector(0f, 0f, -1f)
        val theta = cos(PI / 4)
        val OC = origin - C

        val a = (direction * V).pow(2) - theta * theta
        val b = 2 * ((direction * V) * (OC * V) - (direction * OC * theta * theta))
        val c = (OC * V).pow(2) - (OC * OC * theta * theta)

        var delta = b * b - 4 * a * c

        if (delta <= 0) return null

        delta = sqrt(delta)
        val t1 = ((-b + delta) / (2 * a)).toFloat()
        val t2 = ((-b + delta) / (2 * a)).toFloat()

        if (t1 in invRay.tMin..invRay.tMax) {

            val cp = origin + direction * t1 - C
            val h = cp * V
            if (h in 0f..1f) {
                val hitPoint1 = invRay.at(t1)
                hits.add(
                    HitRecord(
                        worldPoint = transformation * hitPoint1,
                        normal = transformation * coneNormal(hitPoint1, invRay.dir),
                        surfacePoint = conePointToUV(hitPoint1),
                        t = t1,
                        ray = ray,
                        shape = this
                    )
                )
            }
        }

        if (t2 in invRay.tMin..invRay.tMax) {

            val cp = origin + direction * t2 - C
            val h = cp * V
            if (h in 0f..1f) {
                val hitPoint2 = invRay.at(t2)
                hits.add(
                    HitRecord(
                        worldPoint = transformation * hitPoint2,
                        normal = transformation * coneNormal(hitPoint2, invRay.dir),
                        surfacePoint = conePointToUV(hitPoint2),
                        t = t2,
                        ray = ray,
                        shape = this
                    )
                )
            }
        }

        if (hits.isEmpty()) return null
        else {
            hits.sortBy { it.t }
            return hits
        }
    }


    override fun rayIntersection(ray: Ray): HitRecord? {
        val invRay = ray.transformation(transformation.inverse())
        val origin = invRay.origin.toVec()
        val direction = invRay.dir
        direction.normalize()

        val C = Vector(0f, 0f, 1f)
        val V = Vector(0f, 0f, -1f)
        val theta = cos(PI / 4)
        val OC = origin - C

        val a = (direction * V).pow(2) - theta * theta
        val b = 2 * ((direction * V) * (OC * V) - (direction * OC * theta * theta))
        val c = (OC * V).pow(2) - (OC * OC * theta * theta)

        var delta = b * b - 4 * a * c

        if (delta <= 0) return null

        delta = sqrt(delta)
        val t1 = ((-b + delta) / (2 * a)).toFloat()
        val t2 = ((-b + delta) / (2 * a)).toFloat()

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
            normal = this.transformation * coneNormal(hitPoint, invRay.dir),
            surfacePoint = conePointToUV(hitPoint),
            t = firstHitT,
            ray = ray,
            shape = this
        )

    }

    private fun coneNormal(point: Point, rayDir: Vector): Normal {
        val result = Normal(point.x, point.y, point.z)
        return if (point.toVec() * rayDir < 0f) {
            result
        } else {
            -result
        }
    }

    private fun conePointToUV(point: Point): Vec2d {
        if (abs(point.z) < 1e-4) {
            val u: Float = (point.x + 1) / 2
            val v: Float = (point.y + 1) / 2
            return Vec2d(u, v)
        } else {
            val u: Float = atan2(point.y, point.x) / (2f * PI.toFloat())
            val v: Float = point.z
            if (u >= 0) {
                return Vec2d(u, v)
            } else {
                return Vec2d(u + 1, v)
            }
        }
    }
}


/**
 * val invRay = ray.transformation(transformation.inverse())
 *
 *         val origin = invRay.origin.toVec()
 *         val direction = invRay.dir
 *         direction.normalize()
 *         val V = Vector(0f, 0f, -1f)
 *         val C = Vector(0f, 0f, 1f)
 *         val theta = cos(PI / 4)
 *         val co = origin - C
 *
 *         //compute the delta
 *         val a = (direction * V).pow(2) - theta * theta
 *         val b = 2 * ((direction * V) * (co * V) - (direction * co) * theta * theta)
 *         val c = (co * V) * (co * V) - (co * co) * theta * theta
 *
 *
 *         val delta = b.pow(2) - 4 * a * c
 *
 *         if (delta <= 0) return null
 *
 *         val t1: Float = (-b - sqrt(delta) / (2 * a)).toFloat()
 *         val t2 = (-b + sqrt(delta) / (2 * a)).toFloat()
 *
 *         val firstHitT: Float = if (invRay.tMin < t1 && t1 < invRay.tMax) {
 *             t1
 *         } else if (invRay.tMin < t2 && t2 < invRay.tMax) {
 *             t2
 *         } else {
 *             return null
 *         }
 *
 *         val hitPoint = invRay.at(firstHitT)
 *         return HitRecord(
 *             worldPoint = this.transformation * hitPoint,
 *             normal = this.transformation * coneNormal(hitPoint, invRay.dir),
 *             surfacePoint = conePointToUV(hitPoint),
 *             t = firstHitT,
 *             ray = ray,
 *             shape = this
 *         )
 *
 */