import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class ConeTest {
    @Test
    fun pointInternal() {
        val cone = Cone()
        assertTrue(cone.pointInternal(Point(0f, 0f, 1f)))
        assertTrue(cone.pointInternal(Point(0.25f, 0.25f, 0f)))
        assertFalse(cone.pointInternal(Point(0.5f, 0.5f, 1f)))
    }

    @Test
    fun rayIntersection(){
        TODO()
    }

    @Test
    fun rayIntersectionList(){

    }

    @Test
    fun transformation(){
        
    }
}