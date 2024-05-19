/**
 * abstract class for shape
 */
interface Shape {

    val material: Material
    fun rayIntersection(ray:Ray): HitRecord?
    fun rayIntersectionList(ray: Ray):List<HitRecord>?

    fun pointInternal(point: Point): Boolean
}