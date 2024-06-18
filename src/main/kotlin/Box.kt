import java.security.cert.PolicyNode

/**
 * Box Class: Derived from Shape, represent a box axis aligned
 *
 * @param transformation: transformation applied to the box
 * @param Pmin: minimum point of the box
 * @param Pmax: maximum point of the box
 * @param material: material of the box
 *
 * @Default Constructor: Pmin=Point(-1f,1f,-1f), Pmax=Point(1f,1f,1f),
 */
class Box(
    val transformation: Transformation = Transformation(),
    val Pmin: Point = Point(-1f, -1f, -1f),
    val Pmax: Point = Point(1f, 1f, 1f), override val material: Material = Material()
) : Shape {

    /**
     * Returns Boolean if Pmin is smaller than Pmax
     */
    private fun checkVertex(): Boolean {
        return Pmin.x < Pmax.x && Pmin.y < Pmax.y && Pmin.z < Pmax.z
    }

    /**
     * Returns Boolean if a point is internal to the box
     */
    override fun pointInternal(point: Point): Boolean {
        if (!checkVertex()) throw IllegalArgumentException("in Box, Pmin must be smaller than Pmax")
        val point1 = transformation.inverse() * point
        if (point1.x in Pmin.x..Pmax.x &&
            point1.y in Pmin.y..Pmax.y &&
            point1.z in Pmin.z..Pmax.z
        ) return true
        else return false
    }


    /**
     * Returns the closest ray intersection with box
     */
    override fun rayIntersection(ray: Ray): HitRecord? {
        if (!checkVertex()) {
            throw IllegalArgumentException("in Box, Pmin must be smaller than Pmax")
        }
        val invRay = ray.transformation(transformation.inverse())
        var t0 = invRay.tMin
        var t1 = invRay.tMax

        /* minDir and maxDir are the direction of the intersection
        - 0 for x
        - 1 for y
        - 2 for z
         */
        var minDir = -1
        var maxDir = -1

        /*
        iterate over x, y and z to evaluate t-values
        at the end if t0 < t1 means that the intervals [t_i(0),t_i(1)]are not disjointed and thus
        the ray intersects the box
        */
        for (i in 0..2) {
            //evaluate t values where the ray intersects the plane of the box for each coordinate xyz
            var tmin = (Pmin[i] - (invRay.origin)[i]) / invRay.dir[i]
            var tmax = (Pmax[i] - (invRay.origin)[i]) / invRay.dir[i]

            //swap tmin and tmax if tmin > tmax
            // (the minor t indicates the possible first point of intersection)
            if (tmin > tmax) {
                val t = tmin
                tmin = tmax
                tmax = t
            }

            //if the new tmin is greater than the previous t0, update t0 and the direction of the intersection
            if (tmin > t0) {
                t0 = tmin
                minDir = i
            }
            //if the new tmax is smaller than the previous t1, update t1 and the direction of the intersection
            if (tmax < t1) {
                t1 = tmax
                maxDir = i
            }
            //if t0 is greater than t1, the ray does not intersect the box
            if (t0 > t1) return null
        }

        //if minDir is -1, the ray intersects the box only at t1
        if (minDir == -1) {
            val hit = invRay.at(t1)
            val normal = getNormal(invRay, maxDir)
            return HitRecord(
                worldPoint = transformation * hit,
                normal = transformation * normal,
                surfacePoint = surfacePoint(hit, normal),
                t = t1,
                ray = ray,
                shape = this
            )
        } else {
            val hit = invRay.at(t0)
            val normal = getNormal(invRay, minDir)
            return HitRecord(
                worldPoint = transformation * hit,
                normal = transformation * normal,
                surfacePoint = surfacePoint(hit, normal),
                t = t0,
                ray = ray,
                shape = this
            )
        }
    }

    /**
     * Returns all the ray intersections with box
     */
    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        if (!checkVertex()) {
            throw IllegalArgumentException("in Box, Pmin must be smaller than Pmax")
        }

        val hits = ArrayList<HitRecord>()
        //transform the system in the coordinates box
        val invRay = ray.transformation(transformation.inverse())
        var t0 = invRay.tMin
        var t1 = invRay.tMax

        /* minDir and maxDir are the direction of the intersection
        - 0 for x
        - 1 for y
        - 2 for z
         */
        var minDir = -1
        var maxDir = -1

        /*
        iterate over x, y and z to evaluate t-values
        at the end if t0 < t1 means that the intervals [t_i(0),t_i(1)]are not disjointed and thus
        the ray intersects the box
        */
        for (i in 0 until 3) {
            //evaluate t values where the ray intersects the plane of the box for each coordinate xyz
            var tmin = (Pmin[i] - (invRay.origin)[i]) / invRay.dir[i]
            var tmax = (Pmax[i] - (invRay.origin)[i]) / invRay.dir[i]

            //swap tmin and tmax if tmin > tmax
            // (the minor t indicates the possible first point of intersection)
            if (tmin > tmax) {
                val t = tmin
                tmin = tmax
                tmax = t
            }

            //if  the new tmin is greater tha t0, update t0 and the direction of intersection
            if (tmin > t0) {
                t0 = tmin
                minDir = i
            }
            //if the new tmax is smaller than the previous t1, update t1 and the direction of the intersection
            if (tmax < t1) {
                t1 = tmax
                maxDir = i
            }
            //if t0 is greater than t1, the ray does not intersect the box
            if (t0 > t1) return null
        }

        //If minDir==-1 means that the intersection occurs only at t1
        if (minDir == -1) {
            val hit1 = invRay.at(t1)
            val normal = getNormal(invRay, maxDir)
            hits.add(
                HitRecord(
                    worldPoint = transformation * hit1,
                    normal = transformation * normal,
                    surfacePoint = surfacePoint(hit1, normal),
                    t = t1,
                    ray = ray,
                    shape = this
                )
            )
            return hits
        }
        //else there are two interactions, and they are both added to intersectionLists
        else {
            val hit0 = invRay.at(t0)
            val normal0 = getNormal(invRay, minDir)
            hits.add(
                HitRecord(
                    worldPoint = transformation * hit0,
                    normal = transformation * normal0,
                    surfacePoint = surfacePoint(hit0, normal0),
                    t = t0,
                    ray = ray,
                    shape = this
                )
            )

            val hit1 = invRay.at((t1))
            val normal1 = getNormal(invRay, maxDir)
            hits.add(
                HitRecord(
                    worldPoint = transformation * hit1,
                    normal = transformation * normal1,
                    surfacePoint = surfacePoint(hit1, normal1),
                    t = t1,
                    ray = ray,
                    shape = this
                )
            )
            return hits
        }
    }

    /**
     * Returns the normal of the box for a given ray and direction
     */
    private fun getNormal(ray: Ray, dir: Int): Normal {
        val normal = when (dir) {
            0 -> Normal(1f, 0f, 0f)  //parallel to x
            1 -> Normal(0f, 1f, 0f)  //parallel to y
            2 -> Normal(0f, 0f, 1f)  //parallel to z
            else -> throw IllegalArgumentException("tDir must be 0, 1 or 2")
        }
        return if (normal * ray.dir < 0) normal else -normal
    }

    /**
     * Return the UV coordinates of the surface point
     */
    private fun surfacePoint(hitPoint: Point, normal: Normal): Vec2d {
        //evaluate which face of the box is hit
        val whichFace = when {
            normal.isClose(Normal(1f, 0f, 0f)) -> 0
            normal.isClose(Normal(-1f, 0f, 0f)) -> 1
            normal.isClose(Normal(0f, 1f, 0f)) -> 2
            normal.isClose(Normal(0f, -1f, 0f)) -> 3
            normal.isClose(Normal(0f, 0f, 1f)) -> 4
            normal.isClose(Normal(0f, 0f, -1f)) -> 5
            else -> throw IllegalArgumentException("normal must be orthogonal to a face of the box")
        }

        return when (whichFace) {
            0 -> Vec2d(u = -hitPoint.z, v = hitPoint.y)
            1 -> Vec2d(u = hitPoint.z, v = hitPoint.y)
            2 -> Vec2d(u = hitPoint.x, v = -hitPoint.z)
            3 -> Vec2d(u = hitPoint.x, v = hitPoint.z)
            4 -> Vec2d(u = hitPoint.x, v = hitPoint.y)
            5 -> Vec2d(u = -hitPoint.x, v = hitPoint.y)
            else -> throw RuntimeException()
        }
    }
}