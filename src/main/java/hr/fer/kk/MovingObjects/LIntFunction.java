/*
 * Created on 20. sij. 2011.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

/*
 * A linear time function for Integer elements 
 */
public class LIntFunction implements IFunction<Integer> {

   private int i0;   // value of the function for time = 0
   private float k;  // unit step of the function (how much it changes in one time unit)

   public LIntFunction() {
      i0 = 0;
      k = 1;
   }
   
   public LIntFunction(int initial, float step) {
      i0 = initial;
      k = step;
   }

   /*
    * A constructor that takes two values for a function and time instants for those vlaues
    */
   public LIntFunction(int i1, TimeInstant t1, int i2, TimeInstant t2) {
      k = (float)(i2 -i1)/(t2.time - t1.time);
      i0 = i1 - (int)(t1.time*k);
   }
   
   public Integer getValue(TimeInstant Itime) {
      return new Integer(i0 + (int)(k*Itime.time));
   }

}
