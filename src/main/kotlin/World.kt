/**
 * class that contains all the shapes in the scene
 */
class World(private var shapes: Array<Shape>) {
    /**
     * add a shape to the shape array of World type
     */
    fun add(shape: Shape){
        shapes += shape
    }

    /**
     * iterates on every shape of the scene and returns the one closest to the origin of the ray
     */
    fun rayIntersection(ray: Ray): HitRecord? {
        var closest: HitRecord? = null
        var intersection: HitRecord?
        for(shape in shapes){
            intersection = shape.rayIntersection(ray)
            if(intersection == null){
                continue
            }
            if(closest == null || intersection.t < closest.t){
                closest = intersection
            }
        }
        return closest
    }
}