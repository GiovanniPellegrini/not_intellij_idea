/**
 * Box class, inherited from shape, represents a box in 3D space identified by two vertex points Pmin and Pmax
 */
class Box(val transformation: Transformation = Transformation(),
          val Pmin: Point = Point(0f,0f,0f),
          val Pmax: Point = Point(0f,0f,0f)): Shape{

    /**
     * checkVertex evaluates if the vertex of the box are correct (i.e. Pmin < Pmax)
     */
    private fun checkVertex(): Boolean{
        return Pmin.x < Pmax.x && Pmin.y < Pmax.y && Pmin.z < Pmax.z
    }


    override fun rayIntersection(ray: Ray): HitRecord? {
        if(!checkVertex()){
            throw IllegalArgumentException("in Box, Pmin must be smaller than Pmax")
        }
        //transform the ray to the box system of coordinates
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
        for (i in 0..2){
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
        var t = t1
        var dir = minDir
        if (minDir == -1) {
            t = t1
            dir = maxDir
        }

        val hit = invRay.at(t)
        val normal = getNormal(invRay, dir)
        return HitRecord(
            worldPoint = transformation * hit,
            normal = transformation * normal,
            surfacePoint = surfacePoint(hit, normal),
            t = t,
            ray = ray)
    }


    /**
     * evaluates the normal of the box, for a given t,
     * if t is relative to x, y or z, the normal will be the respective axis
     */
    private fun getNormal(ray : Ray, dir : Int): Normal {
        val normal = when(dir){
            0 -> Normal(1f,0f,0f)  //parallel to x
            1 -> Normal(0f,1f,0f)  //parallel to y
            2 -> Normal(0f,0f,1f)  //parallel to z
            else -> throw IllegalArgumentException("tDir must be 0, 1 or 2")
        }

        return if(normal * ray.dir < 0) normal else -normal

    }

    /**
     * evaluates the point on a surface of the box, for a given t,
     * if t is relative to x, y or z, the normal will be the respective axis
     */
    private fun surfacePoint(hitPoint: Point, normal: Normal): Vec2d{
        //evaluate which face of the box is hit
        val whichFace = when{
            normal.isClose(Normal(1f,0f,0f)) -> 0
            normal.isClose(Normal(-1f,0f,0f)) -> 1
            normal.isClose(Normal(0f,1f,0f)) -> 2
            normal.isClose(Normal(0f,-1f,0f)) -> 3
            normal.isClose(Normal(0f,0f,1f)) -> 4
            normal.isClose(Normal(0f,0f,-1f)) -> 5
            else -> throw IllegalArgumentException("normal must be orthogonal to a face of the box")
        }

        /*
        Here we are using cube mapping (https://en.wikipedia.org/wiki/Cube_mapping)
        We decompose a cube in 6 faces, each identified by its normal.
        depending on the face of the box we evaluate the coordinates u and v by rescaling the
        hit point coordinates to the size of the rectangle
        */

        /*
        return when (whichFace) {
            0 -> Vec2d(u = 0.5f + (Pmax.z - hitPoint.z)/(Pmax.z-Pmin.z) * 0.25f, v = 0.75f - (hitPoint.y - Pmin.y)/(Pmax.y-Pmin.y) * 0.25f)
            1 -> Vec2d(u = (hitPoint.z - Pmin.z)/(Pmax.z-Pmin.z) * 0.25f, v = 0.75f - (hitPoint.y - Pmin.y)/(Pmax.y-Pmin.y) * 0.25f)
            2 -> Vec2d(u = 0.25f + (hitPoint.x - Pmin.x)/(Pmax.x-Pmin.x) * 0.25f, v = 0.5f - (Pmax.z - hitPoint.z)/(Pmax.z-Pmin.z) * 0.25f)
            3 -> Vec2d(u = 0.25f + (hitPoint.x - Pmin.x)/(Pmax.x-Pmin.x) * 0.25f, v = 1.00f - (hitPoint.z - Pmin.z)/(Pmax.z-Pmin.z) * 0.25f)
            4 -> Vec2d(u = 0.25f + (hitPoint.x - Pmin.x)/(Pmax.x-Pmin.x) * 0.25f, v = 0.75f - (hitPoint.y - Pmin.y)/(Pmax.y-Pmin.y) * 0.25f)
            5 -> Vec2d(u = 0.75f + (Pmax.x - hitPoint.x)/(Pmax.x-Pmin.x) * 0.25f, v = 0.75f - (hitPoint.y - Pmin.y)/(Pmax.y-Pmin.y) * 0.25f)
            else -> throw RuntimeException()
        }

         */
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