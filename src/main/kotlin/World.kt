/**
 * class that contains all the shapes in the scene
 */
class World(var shapes: Array<Shape> = emptyArray<Shape>(),
            var pointLights: Array<PointLight> = emptyArray<PointLight>()) {
    /**
     * add a shape to the shape array of World type
     */
    fun addShape(shape: Shape){
        shapes += shape
    }

    /**
     * add a point light to the pointLights array of World type
     */
    fun addPointLight(pointLight: PointLight) {
        pointLights += pointLight
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

    fun isPointVisible(point: Point, obsPos: Point): Boolean {
        val dir = point - obsPos
        val dirNorm = dir.norm()

        val ray = Ray(origin = obsPos,dir = dir, tMin = 1e-2f/dirNorm, tMax = 1f )
        for (shape in shapes){
            if(!shape.quickRayIntersection(ray)){
                return false
            }
        }
        return true
    }
}