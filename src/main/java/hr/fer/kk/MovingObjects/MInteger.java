/*
 * Created on 19. svi. 2010.
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

public class MInteger implements Movable {
    public int value;
    
    public MInteger(int _value) {
       this.value = _value;
    }
    
    public String toString() {
       return Integer.toString(value);
    }

   public MInteger getValue(TimeInstant t) {
      // return new MInteger(value);
      return this;
   }
}
