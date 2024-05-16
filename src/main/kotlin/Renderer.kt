abstract class Renderer(val world: World, val backgroundColor: Color = Color()){
    abstract fun render(ray: Ray): Color
}

class OnOffRenderer(world: World, backgroundColor: Color = Color()): Renderer(world = world, backgroundColor = backgroundColor){
     override fun render(ray: Ray): Color{
        return if (world.rayIntersection(ray) != null) Color(255f,255f,255f) else backgroundColor
    }
}

class FlatRenderer(world: World, backgroundColor: Color = Color()): Renderer(world = world, backgroundColor = backgroundColor){
    override fun render(ray: Ray): Color {
        val intersection = world.rayIntersection(ray) ?: return backgroundColor
        val mat = intersection.shape.material
        return mat.brdf.p.getColor(intersection.surfacePoint) + mat.emittedRad.getColor(intersection.surfacePoint)
    }
}