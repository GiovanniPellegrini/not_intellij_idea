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
        world.addShape(sphere)
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
        world.addShape(sphere)
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

    @Test
    fun furnaceTest(){
        val pcg=PCG()

        for(i in 0 until 1){
            val world=World()
            val emittedRad=pcg.randomFloat()
            val rad=pcg.randomFloat()*0.9f
            val material=Material(DiffusionBRDF(UniformPigment(Color(1f,1f,1f)*rad)), emittedRad = UniformPigment(Color(1f,1f,1f)*emittedRad))
            
            val pathTracer=PathTracer(world, maxDepth = 100, russianRouletteLimit = 101, pcg = pcg, numberOfRays = 1)
            val ray=Ray(Point(0f,0f,0f),Vector(1f,0f,0f))
            world.addShape(Sphere(material = material))
            val color=pathTracer.render(ray)

            val expected=emittedRad/(1-rad)
            println(color.r)
            println(expected)
            assert(areClose(expected,color.r))
            assert(areClose(expected,color.g))
            assert(areClose(expected,color.b))

        }
    }
}