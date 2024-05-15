import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PigmentTest{
    @Test
    fun testUniformPigment(){
        val color = Color(1f, 2f, 3f)
        val pigment = uniformPigment(color=color)
        assert(pigment.getColor(Vec2d(0f, 0f)) == color)
        assert(pigment.getColor(Vec2d(1f, 0f)) == color)
        assert(pigment.getColor(Vec2d(0f, 1f)) == color)
        assert(pigment.getColor(Vec2d(1f, 1f)) == color)

    }

    @Test
    fun testCheckeredPigment(){
        val color1 = Color(1f, 2f, 3f)
        val color2 = Color(10f, 20f, 30f)
        val pigment = checkeredPigment(color1=color1, color2=color2, steps=2)

        assert(pigment.getColor(Vec2d(0.25f, 0.25f)) == color1)
        assert(pigment.getColor(Vec2d(0.75f, 0.25f)) == color2)
        assert(pigment.getColor(Vec2d(0.25f, 0.75f)) == color2)
        assert(pigment.getColor(Vec2d(0.75f, 0.75f)) == color1)
    }

    @Test
    fun testImagePigment(){
        val image = HdrImage(width=2, height=2)
        image.setPixel(0, 0, Color(1f, 2f, 3f))
        image.setPixel(1, 0, Color(2f, 3f, 1f))
        image.setPixel(0, 1, Color(2f, 1f, 3f))
        image.setPixel(1, 1, Color(3f, 2f, 1f))

        val pigment = imagePigment(image)
        assert(pigment.getColor(Vec2d(0f, 0f)) == (Color(1f, 2f, 3f)))
        assert(pigment.getColor(Vec2d(1f, 0f)) == (Color(2f, 3f, 1f)))
        assert(pigment.getColor(Vec2d(0f, 1f)) == (Color(2f, 1f, 3f)))
        assert(pigment.getColor(Vec2d(1f, 1f)) == (Color(3f, 2f, 1f)))


    }

}