/**
 * class Triangle implements Triangle (Shape) inherited. It is defined by three points a,b,c representing the vertices of the triangle.
 * The class has a transformation field that is used to transform the triangle.
 * The intersection between a ray and a triangle is calculated using the Kramer's rule.
 */

class Triangle(val transformation: Transformation = Transformation(),
               val a: Point = Point(0f,0f,0f),
               val b: Point = Point(0f,0f,0f),
               val c: Point = Point(0f,0f,0f),override val material: Material=Material()): Shape {

    /**
     * override PointInternal. However, is not useful for this class because triangle are not used in CSG.
     */
    override fun pointInternal(point: Point): Boolean {
        return false
    }

    /**
     * returns True if beta and gamma are in the range [0,1]
      */
    private fun isInside(beta: Float, gamma: Float): Boolean{
        return (beta in (0f..1f)) &&
                (gamma in (0f..1f))
    }

    /**
     * returns the point of the triangle for a given beta and gamma
     */
    fun trianglePoint(beta: Float, gamma: Float ): Point{
        //maybe we should check if beta+gamma<=1 and beta,gamma>=0
        return a + (b-a) * beta + (c-a) * gamma
    }

    /**
     * returns the normal of the triangle normalized
     */
    private fun getNormal(): Normal{
        return ((b-a).xTimes(c-a)).toNormal().normalize()
    }

    /**
     * returns the normal of the triangle for a given point and ray direction
     */
    private fun triangleNormal(point: Point, rayDir: Vector): Normal{
        val result = getNormal()
        return if (point.toVec()*rayDir>0f){
            result
        } else{
            -result
        }
    }

    /**
     * kramerRule returns the solution of the system of  intersection equations for a given ray
     */
    //solution is a vector with components (beta,gamma,t)
    private fun kramerRule(ray: Ray): FloatArray?{
        val solution = floatArrayOf(0f,0f,0f)

        /*we evaluate with kramer's method the system of equations:
                    A+beta(B-A)+gamma(C-A)=O+Td
         */
        val matrix = arrayOf(floatArrayOf(b.x - a.x, c.x - a.x, ray.dir.x),
                             floatArrayOf(b.y - a.y, c.y - a.y, ray.dir.y),
                             floatArrayOf(b.z - a.z, c.z - a.z, ray.dir.z))

        val det = determinant3x3(matrix)
        if(areClose(0f,det)) return null


        for (j in 0..2) {
            val newMatrix = matrix.map { it.copyOf() }.toTypedArray()
            for (i in newMatrix.indices) {
                newMatrix[i][j] = floatArrayOf(ray.origin.x - a.x, ray.origin.y - a.y, ray.origin.z - a.z)[i]
            }
            solution[j] = determinant3x3(newMatrix) / det

        }

        solution[2] *= -1f

        if(solution[2]<ray.tMax && solution[2]>ray.tMin
            && solution[1] in 0f..1f
            && solution[0] in 0f..1f
            && solution[0]+solution[1]<=1f)
        {
            return solution
        }

        return null
    }

    /**
     * evaluates if a ray intersect the triangle and returns the closest intersection to the point of view
     */
    override fun rayIntersection(ray: Ray): HitRecord? {
        val invRay = ray.transformation(transformation.inverse())
        val solution = kramerRule(invRay) ?: return null
        val beta = solution[0]
        val gamma = solution[1]
        val t = solution[2]

        if(t in (invRay.tMin..invRay.tMax) && isInside(beta,gamma)){
            return HitRecord(worldPoint = this.transformation * invRay.at(t),
                            normal = this.transformation * this.triangleNormal(invRay.at(t),ray.dir),
                            surfacePoint = Vec2d(beta, gamma),
                            t = t,
                            ray = ray,
                            shape=this)
        }
        return null
    }
    /**
     * evaluates if a ray intersect the triangle and returns all the intersection from the point of view
     */
    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        val hits= ArrayList<HitRecord>()
        if(this.rayIntersection(ray)==null) return null
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
        if(t in (invRay.tMin..invRay.tMax) && isInside(beta,gamma)){
            return true
        }
        return false
    }


    /**
     * evaluates the determinant of a 3x3 matrix
     */

    /*maybe we should move this function to a helper file the same as are_close and determinant3x3
    in TriangleTest.kt*/
    fun determinant3x3(matrix: Array<FloatArray>): Float {
        return matrix[0][0]*matrix[1][1]*matrix[2][2]+
               matrix[0][1]*matrix[1][2]*matrix[2][0]+
               matrix[0][2]*matrix[1][0]*matrix[2][1]-
               matrix[0][2]*matrix[1][1]*matrix[2][0]-
               matrix[0][1]*matrix[1][0]*matrix[2][2]-
               matrix[0][0]*matrix[1][2]*matrix[2][1]
    }

}