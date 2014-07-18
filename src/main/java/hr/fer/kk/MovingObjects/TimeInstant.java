/*
 * Created on 2009.01.09
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

/*
 * A value of time instant can only be equal to or greater then zero.
 * Also a value -1 i allowed meaning that time instant is undefined
 */
public class TimeInstant {

   public float time;

   public TimeInstant(float _time) {
      this.time = _time;
   }
        
   public String toString() {
      if (time == -1) return "Undefined";
      return Float.toString(time);
   }
}


