 class CSGUnion(val shape1:Shape, val shape2:Shape,val transformation: Transformation=Transformation(), override val material: Material=Material()):Shape {

     override fun rayIntersection(ray: Ray): HitRecord? {
         TODO("Not yet implemented")
     }

     override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
         TODO("Not yet implemented")
     }

}