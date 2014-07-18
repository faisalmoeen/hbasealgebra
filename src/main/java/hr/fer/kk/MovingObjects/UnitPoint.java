/*
 * Created on 1. lip. 2010.
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

public class UnitPoint extends UnitObject<MPoint> {

   public MPoint startp;
   public MPoint endp;

   public UnitPoint(TimeInterval interval, MPoint value) {
      super(interval, value);
   }

   public MPoint getValue(float time) {
      MPoint retval = new MPoint(0,0);
      if (interval.inside(time)) {
         retval.x = startp.x + (endp.x - startp.x)*(time - interval.start)/(interval.length());
         retval.y = startp.y + (endp.y - startp.y)*(time - interval.start)/(interval.length());
         return retval;
      }
      else return null;
   }
   
}
