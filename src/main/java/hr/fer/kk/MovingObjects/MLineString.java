/*
 * Created on 19. svi. 2010.
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MLineString implements Movable {
    public List<MPoint> pointList;
    
    public MLineString() {
       pointList = new ArrayList<MPoint>(10);
    }
    
    public void addPoint(MPoint point) {
       pointList.add(point);
    }
    
    public void addPoint(float x, float y) {
       MPoint point = new MPoint(x, y);
       pointList.add(point);
    }
    
    public String toString() {
       StringBuffer sb = new StringBuffer("");
       sb.append("LineString(");
       ListIterator<MPoint> iterator = pointList.listIterator();
       MPoint point = null;
       for (; iterator.hasNext(); point = iterator.next()) {
          sb.append(point.toString() + ", ");
       }
       sb.append(")");
       return sb.toString();
    }

   public Movable getValue(TimeInstant t) {
      return this;
   }

}
