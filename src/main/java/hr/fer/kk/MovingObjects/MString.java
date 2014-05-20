/*
 * Created on 19. svi. 2010.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

public class MString implements Movable {
    public String value;
    
    public MString(String _value) {
       this.value = _value;
    }
    
    public String toString() {
       return value;
    }

   public MString getValue(TimeInstant t) {
      return this;
   }
}
