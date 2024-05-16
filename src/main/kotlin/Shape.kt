/**
 * abstract class for shape
 */
interface Shape {
    fun rayIntersection(ray:Ray): HitRecord?
    val material: Material
}