/**
 * HitRecord class: Represents the information of a ray intersection with a shape
 *
 * @param worldPoint: intersection point in 3D coordinates
 * @param normal: normal vector at the intersection point
 * @param surfacePoint: intersection point in surface 2D coordinates
 * @param t: distance from the ray origin to the intersection point
 * @param ray: ray that intersects the shape
 * @param shape: shape that is intersected
 */
class HitRecord(
    var worldPoint: Point,
    var normal: Normal,
    var surfacePoint: Vec2d,
    var t: Float, var ray: Ray,
    var shape: Shape
) {


    /**
     * Returns Boolean if a point is internal to the shape
     */
    fun isClose(other: HitRecord): Boolean {
        return this.worldPoint.isClose(other.worldPoint) &&
                this.normal.isClose(other.normal) &&
                this.surfacePoint.isClose(other.surfacePoint) &&
                this.ray.isClose(other.ray) &&
                areClose(this.t, other.t)
    }

}