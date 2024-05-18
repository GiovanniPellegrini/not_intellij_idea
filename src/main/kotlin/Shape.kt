/**
 * abstract class for shape
 */
interface Shape {
    fun rayIntersection(ray:Ray): HitRecord?
    fun rayIntersectionList(ray: Ray):List<HitRecord>?
    val material: Material
}