/*
 * Created on 19. svi. 2010.
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

import java.lang.Math;
import java.util.Iterator;
import java.util.ListIterator;
/*
 * A class that contains only static methods for performing different operations on data
 */
public class Operations {

   public static double Distance(MPoint point1, MPoint point2) {
      return Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
   }
   
   public static MovingObject<Float> Distance(MovingObject<MPoint> mpoint1, MovingObject<MPoint> mpoint2) throws IntervalOverlapException {
      MovingObject<Float> result = new MovingObject<Float>();
      Iterator<UnitObject<MPoint>> iterator1 = mpoint1.getIterator();
      Iterator<UnitObject<MPoint>> iterator2 = mpoint2.getIterator();
      UnitObject<MPoint> unit1, unit2;
      MPoint point1, point2;
      TimeInterval interval1, interval2, intervalr;
      Float valuer;
      UnitObject<Float> unitr;

      if (iterator1.hasNext() && iterator2.hasNext()) {
         unit1 = iterator1.next();
         unit2 = iterator2.next();
         point1 = unit1.getValue();
         point2 = unit2.getValue();
         interval1 = unit1.interval;
         interval2 = unit2.interval;
      }
      else {
         return result;
      }

      while (unit1 != null && unit2 != null) {
         if (interval1.end < interval2.start) {    // disjoined intervals
            if (!iterator1.hasNext()) return result;
            unit1 = iterator1.next();
            point1 = unit1.getValue();
            interval1 = unit1.interval;
         }
         else if (interval2.end < interval1.start) {   // disjoined intervals
            if (!iterator2.hasNext()) return result;
            unit2 = iterator2.next();
            point2 = unit2.getValue();
            interval2 = unit2.interval;
         } 
         // interval2 within interval1
         else if (interval2.start >= interval1.start && interval2.end <= interval1.end){
            intervalr = new TimeInterval(interval2.start, interval2.end);
            valuer = new Float((float)Distance(point1, point2));
            unitr = new UnitObject<Float>(intervalr, valuer);
            result.addUnit(unitr);
            if (!iterator2.hasNext()) return result;
            unit2 = iterator2.next();
            point2 = unit2.getValue();
            interval2 = unit2.interval;
         }
         // interval1 within interval2
         else if (interval1.start >= interval2.start && interval1.end <= interval2.end){
            intervalr = new TimeInterval(interval1.start, interval1.end);
            valuer = new Float((float)Distance(point1, point2));
            unitr = new UnitObject<Float>(intervalr, valuer);
            result.addUnit(unitr);
            if (!iterator1.hasNext()) return result;
            unit1 = iterator1.next();
            point1 = unit1.getValue();
            interval1 = unit1.interval;
         }
         // interval1 starts before interval2
         else if (interval1.start <= interval2.start && interval1.end > interval2.start) {
            intervalr = new TimeInterval(interval2.start, interval1.end);
            valuer = new Float((float)Distance(point1, point2));
            unitr = new UnitObject<Float>(intervalr, valuer);
            result.addUnit(unitr);
            if (!iterator1.hasNext()) return result;
            unit1 = iterator1.next();
            point1 = unit1.getValue();
            interval1 = unit1.interval;
         }
         // interval2 starts before interval1
         else if (interval2.start <= interval1.start && interval2.end > interval1.start) {
            intervalr = new TimeInterval(interval1.start, interval2.end);
            valuer = new Float((float)Distance(point1, point2));
            unitr = new UnitObject<Float>(intervalr, valuer);
            result.addUnit(unitr);
            if (!iterator2.hasNext()) return result;
            unit2 = iterator2.next();
            point2 = unit2.getValue();
            interval2 = unit2.interval;
         }
         // this shouldnt happen because all situations are covered before
         else {
            throw new IntervalOverlapException();
         }
      }
      
      return result;
   }
   
   public static MLineString trajectory(MovingObject<MPoint> mpoint) {
      MLineString lstring = new MLineString();
      //placeholder
      return lstring; 
   }
}
