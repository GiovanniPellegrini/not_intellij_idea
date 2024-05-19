/**
 * CSG= CONSTRUCTIVE SOLID GEOMETRY
 *
 * This class is the implementation of the operation Union between shapes
 */
class CSGIntersection (val shape1:Shape, val shape2:Shape,val transformation: Transformation=Transformation(), override val material: Material=Material()):Shape {

    override fun pointInternal(point: Point): Boolean {
        if(shape1.pointInternal(transformation.inverse()*point) && shape2.pointInternal(transformation.inverse()*point)) return true
        else return false
    }

    override fun rayIntersection(ray: Ray): HitRecord? {
        val invRay=ray.transformation(transformation.inverse())
        val hit1=shape1.rayIntersectionList(invRay)
        val hit2=shape2.rayIntersectionList(invRay)

        //the ray must intersect both shapes
        if(hit1==null) return null
        if(hit2==null) return null


        val hit1min= hit1.minBy { it.t }
        val hit1max=hit1.maxBy { it.t }
        val hit2min=hit2.minBy { it.t }
        val hit2max=hit2.minBy { it.t }

        //returns the first intersection that is contained in the interval of the other shape
        if(hit1min.t in hit2min.t..hit2max.t) return transformation*hit1min
        else if (hit2min.t in hit1min.t..hit1max.t) return transformation*hit2min
        else return null
    }


    //returns all intersections that are on the edge of one shape and contained in the other and vice versa
    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        val hits= mutableListOf<HitRecord>()
        val invRay=ray.transformation(transformation.inverse())
        val hit1=shape1.rayIntersectionList(invRay)
        val hit2=shape2.rayIntersectionList(invRay)

        //the ray must intersect both shapes
        if(hit1!=null) {
            for (h in hit1) {
                if (shape2.pointInternal(h.worldPoint)) hits.add(transformation * h)
            }
        }
        if(hit1!=null) {
            for (h in hit1) {
                if (shape2.pointInternal(h.worldPoint)) hits.add(transformation * h)
            }
        }
        if(hit2!=null) {
            for (h in hit2) {
                if (shape2.pointInternal(h.worldPoint)) hits.add(transformation * h)
            }
        }
        if(hits.isEmpty()) return null
        else{
            hits.sortBy { it.t }
            return hits
        }
    }
}