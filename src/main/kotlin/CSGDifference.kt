/**
 * CSG= CONSTRUCTIVE SOLID GEOMETRY
 *
 * This class is the implementation of the operation Difference between shapes**
 * Shape2 is subtracted from shape1
 *
 * It's useful to understand the code to consult  the slides #6 (for @rayIntersection) and #17 (for @rayListIntersection)
 * at the link https://web.cse.ohio-state.edu/~parent.1/classes/681/Lectures/19.RayTracingCSG.pdf
 */

class CSGDifference(
    val shape1: Shape,
    val shape2: Shape,
    override val transformation: Transformation = Transformation(),
    override val material: Material = Material()
) : Shape {

    override fun pointInternal(point: Point): Boolean {
        if (shape1.pointInternal(transformation * point) && !shape2.pointInternal(transformation * point)) return true
        else return false
    }

    override fun rayIntersection(ray: Ray): HitRecord? {
        val hits = this.rayIntersectionList(ray)
        return hits?.get(0)
    }

    /**
     * return all the intersection that can be:
     * - on the surface of the first shape and not inside the second shape
     * - on the surface of the second shape and inside the first shape
     */

    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        val hits = mutableListOf<HitRecord>()
        val invRay = ray.transformation(transformation.inverse())
        val hit1 = shape1.rayIntersectionList(invRay)
        val hit2 = shape2.rayIntersectionList(invRay)

        if (hit1 != null) {
            for (h in hit1) {
                if (!shape2.pointInternal(h.worldPoint)) hits.add(transformation * h)
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
}