/**
 * CSG= CONSTRUCTIVE SOLID GEOMETRY
 *
 * This class is the implementation of the operation Union between shapes
 */

 class CSGUnion(val shape1:Shape, val shape2:Shape,val transformation: Transformation=Transformation(), override val material: Material=Material()):Shape {

     override fun pointInternal(point: Point): Boolean {
         if(shape1.pointInternal(transformation.inverse()*point) || shape2.pointInternal(transformation.inverse()*point)) return true
         else return false
     }

    /**
     * function to calculate the HitRecord given a ray
     */
     override fun rayIntersection(ray: Ray): HitRecord? {
         val hitsTot= mutableListOf<HitRecord>()
         val invRay=ray.transformation(transformation.inverse())
         val hits1=shape1.rayIntersectionList(invRay)
         val hits2=shape2.rayIntersectionList(invRay)

         if(hits1!=null) hitsTot.addAll(hits1)
         if(hits2!=null) hitsTot.addAll(hits2)
         if(hitsTot.isEmpty()) return null

         var union= hitsTot[0]
         for(hit in hitsTot){
             if(hit.t<union.t) union=hit
         }
         return transformation*union
     }


    //return all the intersection on the board of both shapes
     override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
         val hits= mutableListOf<HitRecord>()
         val invRay=ray.transformation(transformation.inverse())
         val hit1=shape1.rayIntersectionList(invRay)?.toList()
         val hit2=shape2.rayIntersectionList(invRay)?.toList()

         if(hit1!=null){
             for(h in hit1)
                 if(!shape2.pointInternal(h.worldPoint)) hits.add(transformation*h)
         }
         if(hit2!=null){
             for(h in hit2)
                 if(!shape1.pointInternal(h.worldPoint)) hits.add(transformation*h)
         }

         if(hits.isEmpty()) return null
         else{
             hits.sortBy { it.t }
             return hits
         }
     }
}