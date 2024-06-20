/**
 * CSG= CONSTRUCTIVE SOLID GEOMETRY
 *
 * This class is the implementation of the operation Intersection between shapes
 *
 * It's useful to understand the code to consult  the slides #5 (for @rayIntersection) and #16 (for @rayListIntersection)
 * at the link https://web.cse.ohio-state.edu/~parent.1/classes/681/Lectures/19.RayTracingCSG.pdf
 */
class CSGIntersection(
    var shape1: Shape,
    var shape2: Shape,
    override val transformation: Transformation = Transformation(),
    override var material: Material = Material()
) : Shape {

    override fun pointInternal(point: Point): Boolean {
        return shape1.pointInternal(transformation.inverse() * point) && shape2.pointInternal(transformation.inverse() * point)
    }

    override fun rayIntersection(ray: Ray): HitRecord? {
        val hits = this.rayIntersectionList(ray)
        return hits?.get(0)
    }

    /**
     * return all the intersections that can be:
     * - on the surface of the first shape and  inside the second shape
     * - on the surface of the second shape and inside the first shape
     */
    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        val hits = mutableListOf<HitRecord>()
        val invRay = ray.transformation(transformation.inverse())
        val hit1 = shape1.rayIntersectionList(invRay)
        val hit2 = shape2.rayIntersectionList(invRay)

        //the ray must intersect both shapes
        if (hit1 != null) {
            for (h in hit1) {
                if (shape2.pointInternal(h.worldPoint)) hits.add(transformation * h)
            }
        }
        if (hit2 != null) {
            for (h in hit2) {
                if (shape1.pointInternal(h.worldPoint)) hits.add(transformation * h)
            }
        }
        if (hits.isEmpty()) return null
        else {
            hits.sortBy { it.t }
            return hits
        }
    }

    override fun quickRayIntersection(ray: Ray): Boolean {
        val invRay=ray.transformation(transformation.inverse())
        val hit1=shape1.rayIntersectionList(invRay)
        val hit2=shape2.rayIntersectionList(invRay)

        if(hit1!=null){
            for(h in hit1){
                if(shape2.pointInternal(h.worldPoint)) return true
            }
        }

        if(hit2!=null){
            for(h in hit2){
                if(shape1.pointInternal(h.worldPoint)) return true
            }
        }

        return false
    }
}


/**
 * val invRay=ray.transformation(transformation.inverse())
 *         val hit1=shape1.rayIntersectionList(invRay)
 *         val hit2=shape2.rayIntersectionList(invRay)
 *
 *         //the ray must intersect both shapes
 *         if(hit1==null) return null
 *         if(hit2==null) return null
 *
 *
 *         val hit1min=hit1.minBy { it.t }
 *         val hit1max=hit1.maxBy { it.t }
 *         val hit2min=hit2.minBy { it.t }
 *         val hit2max=hit2.minBy { it.t }
 *
 *         //returns the first intersection that is contained in the interval of the other shape
 *         if(hit1min.t in hit2min.t..hit2max.t && shape2.pointInternal(hit1min.worldPoint)) return transformation*hit1min
 *         else if (hit2min.t in hit1min.t..hit1max.t && shape1.pointInternal(hit2min.worldPoint)) return transformation*hit2min
 *         else return null
 */