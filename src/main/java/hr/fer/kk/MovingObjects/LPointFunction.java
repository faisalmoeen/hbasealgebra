/*
 * Created on 20. sij. 2011.
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

public class LPointFunction implements IFunction<MPoint> {

   private MPoint i0;   // value of the function for time = 0
   private float kx;     // unit step of the function in x direction
   private float ky;     // unit step of the function in y direction

   public LPointFunction() {
      i0 = new MPoint(0, 0);
      kx = 1;
      ky = 1;
   }
   
   public LPointFunction(MPoint initial, float stepx, float stepy) {
      i0 = initial;
      kx = stepx;
      ky = stepy;
   }

   /*
    * A constructor that takes two values for a function and time instants for those vlaues
    */
   public LPointFunction(MPoint point1, TimeInstant t1, MPoint point2, TimeInstant t2) {
      kx = (point2.x - point1.x)/(t2.time - t1.time);
      ky = (point2.y - point1.y)/(t2.time - t1.time);
      float x0, y0;
      x0 = point1.x - t1.time*kx;
      y0 = point1.y - t1.time*ky;
      i0 = new MPoint(x0, y0);
   }
   
   public MPoint getValue(TimeInstant Itime) {
//      return new Integer(i0 + (int)(k*Itime.time));
      float x, y;
      x = i0.x + kx*Itime.time;
      y = i0.y + ky*Itime.time;
      return new MPoint(x, y);      
   }

}
