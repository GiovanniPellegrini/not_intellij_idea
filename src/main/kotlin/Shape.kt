/**
 * abstract class for shape
 */
interface Shape {
    val material: Material
    val transformation: Transformation
    fun rayIntersection(ray:Ray): HitRecord?
    fun rayIntersectionList(ray: Ray):List<HitRecord>?
    fun pointInternal(point: Point): Boolean
    fun quickRayIntersection(ray: Ray): Boolean
}