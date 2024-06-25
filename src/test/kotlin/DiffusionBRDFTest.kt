import org.junit.jupiter.api.Test
import kotlin.math.PI

class DiffusionBRDFTest {


    @Test
    fun testDiffusionBRDF() {
        val pcg = PCG()
        val rayList = mutableListOf<Ray>()
        val hitList = mutableListOf<HitRecord>()
        val sphere = Sphere()
        val plane = Plane()
        for (i in 0..999999) {

            rayList.add(
                plane.material.brdf.scatterRay(
                    pcg = pcg,
                    incomingDir = Vector(0f, 0f, -1f),
                    interactionPoint = Point(0f, 0f, 0f),
                    normal = Normal(0f, 0f, 1f),
                    depth = 1
                )
            )

            hitList.add(sphere.rayIntersection(rayList[i])!!)

        }

        var countGreater = 0
        var countLesser = 0
        for (hitRecord in hitList) {
            if (hitRecord.worldPoint.z > kotlin.math.cos(PI/4f)) { // this number is cos(PI/4)
                countGreater++
            } else {
                countLesser++
            }
        }

        assert(countGreater == 499783)
        assert(countLesser == 500217)

    }
}
