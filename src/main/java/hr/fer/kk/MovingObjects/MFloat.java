/*
 * Created on 19. svi. 2010.
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

public class MFloat implements Movable {
    public float value;
    
    public MFloat(float _value) {
       this.value = _value;
    }
    
    public String toString() {
       return Float.toString(value);
    }

   public MFloat getValue(TimeInstant t) {
      // return new MFloat(value);
      return this;
   }
}
