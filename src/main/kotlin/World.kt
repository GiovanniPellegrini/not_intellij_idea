/**
 * World class: contains an array of shapes
 *
 * @default shapes: empty array of shapes
 */
class World(var shapes: Array<Shape> = emptyArray<Shape>()) {

    /**
     * Adds a shape to the world
     */
    fun add(shape: Shape) {
        shapes += shape
    }

    /**
     * Evaluates the closest ray intersection with the world
     */
    fun rayIntersection(ray: Ray): HitRecord? {
        var closest: HitRecord? = null
        var intersection: HitRecord?
        for (shape in shapes) {
            intersection = shape.rayIntersection(ray)
            if (intersection == null) {
                continue
            }
            if (closest == null || intersection.t < closest.t) {
                closest = intersection
            }
        }
        return closest
    }
}