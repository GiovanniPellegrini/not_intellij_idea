import org.junit.jupiter.api.Test

class PathTracingTest {
    @Test
    fun furnaceTest() {
        val sphere = Sphere()
        val pcg = PCG()

        for (i in 0 until 5) {
            val world = World()
            val emittedRad = pcg.randomFloat()
            val rad = pcg.randomFloat() * 0.9f

            val material = Material(
                brdf = DiffusionBRDF(UniformPigment(Color(1f, 1f, 1f) * rad)),
                emittedRad = UniformPigment(Color(1f, 1f, 1f) * emittedRad)
            )

            world.add(sphere)
            val pathTracer = PathTracer(world, maxdepth = 100, russianRouletteLimit = 101, pcg = pcg)
            val ray = Ray(Point(0f, 0f, 0f), Vector(0f, 1f, 0f))

            val color = pathTracer.render(ray)

        }
    }
}