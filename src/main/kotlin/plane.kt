class plane(val transformation: Transformation = Transformation(),
            val origin: Point = Point(),
            val vector: Vector = Vector()): Shape{

    /**
     * constructor for xy plane
     */
    constructor(transformation: Transformation): this(transformation,
                                                      Point(0f,0f,0f),
                                                      Vector(0f,0f,1f))

    override fun rayIntersection(ray: Ray): HitRecord? {
        return null
    }

}