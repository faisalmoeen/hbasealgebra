/*
 * Created on 19. svi. 2010.
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

public class MBoolean implements Movable {
    public boolean value;
    
    public MBoolean(boolean _value) {
       this.value = _value;
    }
    
    public String toString() {
       return Boolean.toString(value);
    }

   public Movable getValue(TimeInstant t) {
      // return new MBoolean(value); 
      return this;
   }

}
