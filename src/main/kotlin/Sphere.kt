import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

class Sphere(val transformation: Transformation = Transformation()): Shape{
    /**
     * evaluates if a ray intersect the sphere and returns the closest intersection to the point of view
     */
    override fun rayIntersection(ray: Ray): HitRecord? {
        //compute delta/4
        val invRay = ray.transformation(transformation.inverse())
        val origin = invRay.origin.toVec()
        val d2 = invRay.dir.sqNorm()
        val b = origin.sqNorm()
        val delta = (origin*invRay.dir)*(origin*invRay.dir)-d2*(b-1f)


        if(delta<=0) return null

        //compute solutions
        val deltaSqr = sqrt(delta)
        val t1 = (-origin*invRay.dir-deltaSqr)/d2
        val t2 = (-origin*invRay.dir+deltaSqr)/d2

        val firstHitT: Float = if(invRay.tMin<t1 && t1<invRay.tMax){
            t1
        } else if (invRay.tMin<t2 && t2<invRay.tMax) {
            t2
        } else{
            return null
        }

        val hitPoint = invRay.at(firstHitT)
        return HitRecord(worldPoint = this.transformation * hitPoint,
                         normal = this.transformation * sphereNormal(hitPoint, ray.dir),
                         surfacePoint = spherePointToUV(hitPoint),
                         t = firstHitT,
                         ray = ray)
    }

    private fun sphereNormal(point: Point, rayDir:Vector): Normal{
        val result = Normal(point.x,point.y,point.z)
        return if (point.toVec()*rayDir<0f){
            result
        } else{
            -result
        }
    }

    private fun spherePointToUV(point: Point): Vec2d{
        val u:Float = atan2(point.y,point.x)/(2f * PI.toFloat())
        return if (u>=0){
            Vec2d(u = u, v = acos(point.z)/ PI.toFloat())
        } else{
            Vec2d(u = u+1f, v = acos(point.z) / PI.toFloat())
        }
    }
}