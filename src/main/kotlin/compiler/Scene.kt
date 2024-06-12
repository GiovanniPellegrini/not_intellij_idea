package compiler
import Material
import World
import Camera

// to be developed
class Scene(val materials: MutableMap<String, Material>,
            val world: World = World(),
            val camera: Camera? = null,
            val floatVariables: MutableMap<String, Float>,
            val overriddenVariables: MutableSet<String>
)
