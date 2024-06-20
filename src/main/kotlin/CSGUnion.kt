/*
 * CSG = CONSTRUCTIVE SOLID GEOMETRY
 *
 * This class is the implementation of the operation Union between shapes
 * Shape2 is combined with shape1
 *
 * It's useful to understand the code to consult the slides at the link https://web.cse.ohio-state.edu/~parent.1/classes/681/Lectures/19.RayTracingCSG.pdf
 */


/**
 * CSGUnion class : derived from Shape, it represents the boolean union composition between two shapes
 *
 * @property shape1: the first shape
 * @property shape2: the second shape that will be combined with the first shape
 * @property transformation: the transformation that will be applied to the shape
 * @property material: the material of the shape
 */
class CSGUnion(
    private val shape1: Shape,
    private val shape2: Shape,
    override val transformation: Transformation = Transformation(),
    override val material: Material = Material()
) : Shape {

    /**
     * Returns Boolean if a point is internal to the CSG
     */
    override fun pointInternal(point: Point): Boolean {
        return shape1.pointInternal(transformation.inverse() * point) || shape2.pointInternal(transformation.inverse() * point)
    }

    /**
     * Returns the closest ray intersection with CSG
     */
    override fun rayIntersection(ray: Ray): HitRecord? {
        val hitsTot = mutableListOf<HitRecord>()
        val invRay = ray.transformation(transformation.inverse())
        val hits1 = shape1.rayIntersectionList(invRay)
        val hits2 = shape2.rayIntersectionList(invRay)

        if (hits1 != null) hitsTot.addAll(hits1)
        if (hits2 != null) hitsTot.addAll(hits2)
        if (hitsTot.isEmpty()) return null

        var union = hitsTot[0]
        for (hit in hitsTot) {
            if (hit.t < union.t) union = hit
        }
        return transformation * union
    }


    /**
     * Returns all the intersection that can be:
     * - on the surface of the first shape and not inside the second shape
     * - on the surface of the second shape and not inside the first shape
     */
    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        val hits = mutableListOf<HitRecord>()
        val invRay = ray.transformation(transformation.inverse())
        val hit1 = shape1.rayIntersectionList(invRay)?.toList()
        val hit2 = shape2.rayIntersectionList(invRay)?.toList()

        if (hit1 != null) {
            for (h in hit1)
                if (!shape2.pointInternal(h.worldPoint)) hits.add(transformation * h)
        }
        if (hit2 != null) {
            for (h in hit2)
                if (!shape1.pointInternal(h.worldPoint)) hits.add(transformation * h)
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
        if (shape1.rayIntersectionList(invRay) != null) return true
        if (shape2.rayIntersectionList(invRay) != null) return true
        return false
    }
}