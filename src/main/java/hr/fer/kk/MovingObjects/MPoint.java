/*
 * Created on 19. svi. 2010.
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

public class MPoint implements Movable {
   public float x;
   public float y;
   
   public MPoint(float _x, float _y) {
      this.x = _x;
      this.y = _y;
   }
   
   public String toString() {
      return "Point(" + x + ", " + y + ")";
   }

   public MPoint getValue(TimeInstant t) {
      return this;
   }
}
