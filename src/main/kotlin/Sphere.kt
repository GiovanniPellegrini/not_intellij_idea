import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

class Sphere(override val transformation: Transformation = Transformation(),
             override val material: Material = Material()): Shape{

    /**
     * check if a point is internal to the sphere
     */
    override fun pointInternal(point: Point): Boolean {
        val point1=transformation.inverse()*point
        if(point1.toVec().sqNorm()<=1f) return true
        else return false
    }
    /**
     * evaluates if a ray intersect the sphere and returns the closest intersection from the point of view
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
                         ray = ray,
                         shape = this)
    }

    /**
     *evaluates if a ray intersect the sphere and returns all the intersection from the point of view
     */

    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {

        val hits= ArrayList<HitRecord>()
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

        if(invRay.tMin<t1 && t1<invRay.tMax){
            val hitPoint1=invRay.at(t1)
            hits.add(HitRecord(worldPoint = this.transformation * hitPoint1,
                normal = this.transformation * sphereNormal(hitPoint1, ray.dir),
                surfacePoint = spherePointToUV(hitPoint1),
                t = t1,
                ray = ray,
                shape = this))
        }
        if(invRay.tMin<t2 && t2<invRay.tMax){
            val hitPoint2=invRay.at(t2)
            hits.add(HitRecord(worldPoint = this.transformation * hitPoint2,
                normal = this.transformation * sphereNormal(hitPoint2, ray.dir),
                surfacePoint = spherePointToUV(hitPoint2),
                t = t2,
                ray = ray,
                shape = this))
        }
        return if (hits.isEmpty()) null
        else hits
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