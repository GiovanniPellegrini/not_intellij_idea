/**
 * type that record information of a ray intersection
 */
class HitRecord(var worldPoint: Point,
                var normal: Normal,
                var surfacePoint: Vec2d,
                var t: Float,var ray: Ray,
                var shape: Shape){


    /**
     * verify if two HitRecord are equals
     */
    fun isClose(other: HitRecord): Boolean {
        return this.worldPoint.isClose(other.worldPoint) &&
               this.normal.isClose(other.normal) &&
               this.surfacePoint.isClose(other.surfacePoint) &&
               this.ray.isClose(other.ray) &&
               areClose(this.t, other.t)
    }

}