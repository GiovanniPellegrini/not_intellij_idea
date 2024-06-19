/**
 * Shape interface: Represents a geometric shape in the scene
 *
 * @param material: the material of the shape
 * @property rayIntersection: function to evaluate the ray intersection with shape
 * @property rayIntersectionList: function to evaluate all the ray intersection with shape
 * @property pointInternal: function to check if a point is internal to the shape
 */
interface Shape {

    val material: Material
    fun rayIntersection(ray: Ray): HitRecord?
    fun rayIntersectionList(ray: Ray): List<HitRecord>?
    val transformation: Transformation
    fun pointInternal(point: Point): Boolean
}