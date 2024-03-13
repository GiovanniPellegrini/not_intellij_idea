import kotlin.math.abs

fun main() {
    println("Hello world")
    var image = HdrImage(4, 4)
    println(image.getPixel(1,1))
}

fun are_close(x: Float, y: Float, epsilon: Float = 1.0E-5F): Boolean {
    return (abs(x-y)<epsilon)
}