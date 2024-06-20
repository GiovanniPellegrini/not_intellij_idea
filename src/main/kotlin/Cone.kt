import kotlin.math.*

/**
 * Cone class: Derived from Shape, represents a cone centered at the origin with a height of 1 and a radius of 1
 */

class Cone(
    override val transformation: Transformation = Transformation(),
    override val material: Material = Material()
) :
    Shape {

    /**
     * Returns ture if a point is inside the cone
     */
    override fun pointInternal(point: Point): Boolean {
        val invPoint = transformation.inverse() * point
        return if (invPoint.z in 0f..1f && invPoint.x.pow(2) + invPoint.y.pow(2) - (invPoint.z - 1f).pow(2) <= 0) true
        else false
    }

    /**
     * Returns all the intersections of a ray with the cone
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
        val t1 = ((-b - delta) / (2 * a)).toFloat()
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


    /**
     * Returns the closest intersection of a ray with the cone
     * To understand the math of the code visit  https://lousodrome.net/blog/light/2017/01/03/intersection-of-a-ray-and-a-cone/
     */
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

        var delta = b * b - (4 * a * c)

        if (delta <= 0) return null

        val deltasqr = sqrt(delta)
        val t1 = ((-b - deltasqr) / (2 * a)).toFloat()
        println(t1)
        val t2 = ((-b + deltasqr) / (2 * a)).toFloat()
        println(t2)

        val firstHitT = min(t1, t2)
        if (firstHitT < invRay.tMin || firstHitT > invRay.tMax) return null
        else {
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
    }

    /**
     * Returns the normal of the cone at a given point and a ray direction
     */
    private fun coneNormal(point: Point, rayDir: Vector): Normal {
        val result = Normal(point.x, point.y, point.z)
        return if (point.toVec() * rayDir < 0f) {
            result
        } else {
            -result
        }
    }

    /**
     * Returns the UV coordinates of a point on the cone
     */
    fun conePointToUV(point: Point): Vec2d {
        if (abs(point.z) < 1e-4) {
            val u: Float = point.x
            val v: Float = point.y
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

    /**
     * Returns true if the ray intersects the cone
     */
    override fun quickRayIntersection(ray: Ray): Boolean {
        val invRay = ray.transformation(transformation.inverse())
        val origin = invRay.origin.toVec()
        val a = invRay.dir.sqNorm()
        val b = 2f * (origin * invRay.dir)
        val c = origin.sqNorm() - 1f

        val delta = b * b - 4f * a * c
        if (delta < 0f) return false

        val sqrtDelta = sqrt(delta)
        val t1 = (-b - sqrtDelta) / (2f * a)
        val t2 = (-b + sqrtDelta) / (2f * a)

        return (invRay.tMin < t1 && t1 < invRay.tMax) or (invRay.tMin < t2 && t2 < invRay.tMax) }
}

