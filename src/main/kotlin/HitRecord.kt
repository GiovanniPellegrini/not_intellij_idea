/**
 * type that record information of a ray intersection
 */
class HitRecord(val worldPoint: Point,
                val normal: Normal,
                val surfacePoint: Vec2d,
                val t: Float,val ray: Ray){

    /**
     * verify if two HitRecord are equals
     */
    fun isClose(other: HitRecord): Boolean {
        return this.worldPoint.isClose(other.worldPoint) &&
               this.normal.isClose(other.normal) &&
               this.surfacePoint.isClose(other.surfacePoint) &&
               this.ray.isClose(other.ray) &&
               are_close(this.t, other.t)
    }
}