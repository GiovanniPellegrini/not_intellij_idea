 class CSGUnion(val shape1:Shape, val shape2:Shape,val transformation: Transformation=Transformation(), override val material: Material=Material()):Shape {

     override fun rayIntersection(ray: Ray): HitRecord? {
         val hitsTot= mutableListOf<HitRecord>()
         val hits1=shape1.rayIntersectionList(ray)
         val hits2=shape2.rayIntersectionList(ray)

         if(hits1!=null) hitsTot.addAll(hits1)
         if(hits2!=null) hitsTot.addAll(hits2)
         if(hitsTot.isEmpty()) return null


         var union= hitsTot[0]
         for(hit in hitsTot){
             if(hit.t<union.t) union=hit
         }
         return union
     }


     override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
         val hits=this.rayIntersection(ray)
         TODO("not implemented yet")
     }

}