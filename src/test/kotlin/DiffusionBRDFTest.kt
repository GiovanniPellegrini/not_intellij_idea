import org.junit.jupiter.api.Test
import kotlin.math.PI
class DiffusionBRDFTest{


    @Test
    fun testDiffusionBRDF(){
        val pcg = PCG()
        val rayList = mutableListOf<Ray>()
        val hitList = mutableListOf<HitRecord>()
        val sphere = Sphere()
        val base = onbFromZ(normal = Normal(0f, 0f, 1f))
        for(i in 0..999) {
            val cosThetaSq = pcg.randomFloat()
            val cosTheta = kotlin.math.sqrt(cosThetaSq)
            val sinTheta = kotlin.math.sqrt(1f - cosThetaSq)
            val rand = pcg.randomFloat()
            val phi = 2f * PI.toFloat() * rand

            rayList.add(Ray(
                origin = Point(0f,0f,0f),
                dir=base.first * kotlin.math.cos(phi) * cosTheta + base.second * kotlin.math.sin(phi) * cosTheta + base.third * sinTheta,
                tMin = 1e-3f,
                tMax = Float.POSITIVE_INFINITY,
                depth = 2
            ))

            hitList.add(sphere.rayIntersection(rayList[i])!!)
        }

        var countGreater = 0
        var countLesser = 0
        for(hitRecord in hitList) {
            if(hitRecord.worldPoint.z> 0.7071067811865476f) { // this number is cos(PI/4)
                countGreater++
            } else {
                countLesser++
            }
        }

        assert(countGreater == 489)
        assert(countLesser == 511)


    }
}
