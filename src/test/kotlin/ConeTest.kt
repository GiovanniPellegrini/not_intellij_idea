import org.junit.jupiter.api.Test

class ConeTest{
    @Test
    fun pointInternalTest(){
        val cone = Cone(transformation = Translation(Vector(1f,0f,3f)), height = 1f, radius = 1f)
        assert(cone.pointInternal(Point(1f,0f,3f)))
        assert(!cone.pointInternal(Point(1f,2f,4f)))

        val cone2 = Cone(transformation = Rotation(Vector(1f,0f,0f),180f), height = 1f, radius = 1f)
        assert(cone2.pointInternal(Point(1f,0f,3f)))
        assert(!cone2.pointInternal(Point(1f,2f,4f)))

    }
}