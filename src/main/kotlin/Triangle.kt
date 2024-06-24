/**
 * Triangle class: derives from Shape, represents a triangle in 3D space
 *
 * @param transformation: transformation applied to the triangle
 * @param a: first vertex of the triangle
 * @param b: second vertex of the triangle
 * @param c: third vertex of the triangle
 * @param material: material of the triangle
 */

class Triangle(
    override val transformation: Transformation = Transformation(),
    val a: Point = Point(0f, 0f, 0f),
    val b: Point = Point(0f, 0f, 0f),
    val c: Point = Point(0f, 0f, 0f), override val material: Material = Material()
) : Shape {

    /**
     * Returns always false, triangles cannot be used for CSG
     */
    override fun pointInternal(point: Point): Boolean {
        return false
    }

    /**
     * Returns Boolean if beta and gamma are in the range [0,1]
     */
    private fun isInside(beta: Float, gamma: Float): Boolean {
        return (beta in (0f..1f)) &&
                (gamma in (0f..1f))
    }

    /**
     * Returns the point of the triangle for a given beta and gamma
     */
    fun trianglePoint(beta: Float, gamma: Float): Point {

        return if (isInside(beta, gamma)) {
            a + (b - a) * beta + (c - a) * gamma
        } else {
            throw IllegalArgumentException("beta and gamma must be in the range [0,1]")
        }
    }

    /**
     * Returns the unit normal of the triangle
     */
    private fun getNormal(): Normal {
        return ((b - a).xTimes(c - a)).toNormal().normalize()
    }

    /**
     * Returns the normal of the triangle for a given point and ray direction
     */
    private fun triangleNormal(point: Point, rayDir: Vector): Normal {
        val result = getNormal()
        return if (point.toVec() * rayDir < 0f) {
            result
        } else {
            -result
        }
    }

    /**
     * Returns the solution of the system of intersection equations for a given ray using Kramer's rule
     */
    //solution is a vector with components (beta,gamma,t)
    private fun kramerRule(ray: Ray): FloatArray? {
        val solution = floatArrayOf(0f, 0f, 0f)

        /*we evaluate with kramer's method the system of equations:
                    A+beta(B-A)+gamma(C-A)=O+Td
         */
        val matrix = arrayOf(
            floatArrayOf(b.x - a.x, c.x - a.x, ray.dir.x),
            floatArrayOf(b.y - a.y, c.y - a.y, ray.dir.y),
            floatArrayOf(b.z - a.z, c.z - a.z, ray.dir.z)
        )

        val det = determinant3x3(matrix)
        if (areClose(0f, det)) return null


        for (j in 0..2) {
            val newMatrix = matrix.map { it.copyOf() }.toTypedArray()
            for (i in newMatrix.indices) {
                newMatrix[i][j] = floatArrayOf(ray.origin.x - a.x, ray.origin.y - a.y, ray.origin.z - a.z)[i]
            }
            solution[j] = determinant3x3(newMatrix) / det

        }

        solution[2] *= -1f

        if (solution[2] < ray.tMax && solution[2] > ray.tMin
            && solution[1] in 0f..1f
            && solution[0] in 0f..1f
            && solution[0] + solution[1] <= 1f
        ) {
            return solution
        }

        return null
    }

    /**
     * Returns the closest intersection of a ray with the triangle
     */
    override fun rayIntersection(ray: Ray): HitRecord? {
        val invRay = ray.transformation(transformation.inverse())
        val solution = kramerRule(invRay) ?: return null
        val beta = solution[0]
        val gamma = solution[1]
        val t = solution[2]

        if (t in (invRay.tMin..invRay.tMax) && isInside(beta, gamma)) {
            return HitRecord(
                worldPoint = this.transformation * invRay.at(t),
                normal = this.transformation * this.triangleNormal(invRay.at(t), invRay.dir),
                surfacePoint = Vec2d(beta, gamma),
                t = t,
                ray = ray,
                shape = this
            )

        }
        return null
    }

    /**
     * Returns all the intersection of a ray with the triangle
     */
    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        val hits = ArrayList<HitRecord>()
        if (this.rayIntersection(ray) == null) return null
        else {
            hits.add(this.rayIntersection(ray)!!)
            return hits
        }
    }

    /**
     * evaluates if a ray intersect the triangle and returns a boolean
     */
    override fun quickRayIntersection(ray: Ray): Boolean {
        val invRay = ray.transformation(transformation.inverse())
        val solution = kramerRule(invRay) ?: return false
        val beta = solution[0]
        val gamma = solution[1]
        val t = solution[2]
        return t in (invRay.tMin..invRay.tMax) && isInside(beta,gamma)
    }
}