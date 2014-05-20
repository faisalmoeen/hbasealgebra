/*
 * Created on 2. lip. 2010.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

/*
 * A simple implementation for distance between two points
 */
public class DistancePP extends MBinaryFunction<Float, MPoint, MPoint> {

   public Float execute(MPoint arg1, MPoint arg2) throws IllegalArgumentException {
      return new Float(Math.sqrt(Math.pow((arg1.x - arg2.x), 2) + Math.pow((arg1.y - arg2.y), 2)));
   }

}
