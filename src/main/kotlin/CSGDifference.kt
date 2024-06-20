/*
 * CSG = CONSTRUCTIVE SOLID GEOMETRY
 *
 * This class is the implementation of the operation Difference between shapes**
 * Shape2 is subtracted from shape1
 *
 * It's useful to understand the code to consult the slides at the link https://web.cse.ohio-state.edu/~parent.1/classes/681/Lectures/19.RayTracingCSG.pdf
 */


/**
 * CSGDifference class : derived from Shape, it represents the boolean difference composition between two shapes
 *
 * @property shape1: the first shape
 * @property shape2: the second shape that will be subtracted from the first shape
 * @property transformation: the transformation that will be applied to the shape
 * @property material: the material of the shape
 */
class CSGDifference(
    private val shape1: Shape,
    private val shape2: Shape,
    override val transformation: Transformation = Transformation(),
    override val material: Material = Material()
) : Shape {

    /**
     * Returns Boolean if a point is internal to the CSG
     */
    override fun pointInternal(point: Point): Boolean {
        return shape1.pointInternal(transformation * point) && !shape2.pointInternal(transformation * point)
    }

    /**
     * Returns the closest ray intersection with CSG
     */
    override fun rayIntersection(ray: Ray): HitRecord? {
        val hits = this.rayIntersectionList(ray)
        return hits?.get(0)
    }

    /**
     * Returns all the intersections with the two shapes:
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

    /**
     * Returns Boolean if a ray intersects the CSG
     */
    override fun quickRayIntersection(ray: Ray): Boolean {
        val invRay = ray.transformation(transformation.inverse())
        val hit1 = shape1.rayIntersectionList(invRay)
        val hit2 = shape2.rayIntersectionList(invRay)

        if (hit1 != null) {
            for (h in hit1) {
                if (!shape2.pointInternal(h.worldPoint)) return true
            }
        }

        if (hit2 != null) {
            for (h in hit2) {
                if (shape1.pointInternal(h.worldPoint)) return true
            }
        }

        return false

    }
}