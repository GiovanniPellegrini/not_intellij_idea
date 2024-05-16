import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RendererTest{
    @Test
    fun testOnOffRenderer() {
        val sphere = Sphere(
            transformation = Translation(Vector(2f, 0f, 0f)) *
                             scalingTransformation(Vector(0.2f, 0.2f, 0.2f)),
            material = Material(brdf = DiffusionBRDF(p = UniformPigment(Color(255f, 255f, 255f))))
        )
        val image = HdrImage(width = 3, height = 3)
        val camera = OrthogonalCamera()
        val tracer = ImageTracer(image = image, camera = camera)
        val world = World()
        world.add(sphere)
        val renderer = OnOffRenderer(world = world)
        tracer.fireAllRays(renderer::render)

        assert(image.getPixel(0, 0) == Color())
        assert(image.getPixel(1, 0) == Color())
        assert(image.getPixel(2, 0) == Color())

        assert(image.getPixel(0, 1) == Color())
        assert(image.getPixel(1, 1) == Color(255f, 255f, 255f))
        assert(image.getPixel(2, 1) == Color())

        assert(image.getPixel(0, 2) == Color())
        assert(image.getPixel(1, 2) == Color())
        assert(image.getPixel(2, 2) == Color())
    }

    @Test
    fun testFlatRenderer(){
        val sphereColor = Color(1.0f, 2.0f, 3.0f)
        val sphere = Sphere(transformation=Translation(Vector(2f, 0f, 0f)) *
                            scalingTransformation(Vector(0.2f, 0.2f, 0.2f)),
            material=Material(brdf=DiffusionBRDF(p=UniformPigment(sphereColor))))
        val image = HdrImage(width=3, height=3)
        val camera = OrthogonalCamera()
        val tracer = ImageTracer(image=image, camera=camera)
        val world = World()
        world.add(sphere)
        val renderer = FlatRenderer(world=world)
        tracer.fireAllRays(renderer::render)

        assert(image.getPixel(0, 0) == Color())
        assert(image.getPixel(1, 0) == Color())
        assert(image.getPixel(2, 0) == Color())

        assert(image.getPixel(0, 1) == Color())
        assert(image.getPixel(1, 1) == sphereColor)
        assert(image.getPixel(2, 1) == Color())

        assert(image.getPixel(0, 2) == Color())
        assert(image.getPixel(1, 2) == Color())
        assert(image.getPixel(2, 2) == Color())
    }
}