/*
 * Created on 2009.01.09
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// public class MovingObject<T extends Movable> {
public class MovingObject<T> {

   public List<UnitObject<T>> unitList;   

   public MovingObject() {
      unitList = new ArrayList<UnitObject<T>>(10);
   }

   /*
    * Creates a new moving object with a constant value on 
    * interval <-INF, +INF>
    */
   public MovingObject(T item) {
      unitList = new ArrayList<UnitObject<T>>(10);
      UnitObject<T> unit = new UnitObject<T>(new TimeInterval(-1, 10000), item);
      unitList.add(unit);
   }

   /*
    * Return iterator used to go over unit objects
    */
   public Iterator<UnitObject<T>> getIterator() {
      return unitList.listIterator();
   }
   
   /* Method that merges adjecent intervals if possible and then sorts intervals
    * according to starting time. If intervals overlap, throws an exception 
    * 
    * ATM merge only checks if intervals overlap, and throws exception if they do
    */
   private void mergeAndSort() throws IntervalOverlapException {
      // sort
      Collections.sort(unitList);

      // merge
      UnitObject<T> unit1, unit2;
      if (unitList.size() > 1) {
         ListIterator<UnitObject<T>> iterator = unitList.listIterator();
         unit1 = iterator.next();
         unit2 = iterator.next();
         if (unit1.interval.overlaps(unit2.interval)){
            throw new IntervalOverlapException(); 
         }
         
         while (iterator.hasNext()) {
            unit1 = unit2;
            unit2 = iterator.next();
            if (unit1.interval.overlaps(unit2.interval)){
               throw new IntervalOverlapException(); 
            }
         }
      }
   }
   
   public T getValue(TimeInstant Itime) {
      T result = null;
      ListIterator<UnitObject<T>> iterator = unitList.listIterator();
      UnitObject<T> unit = null;
      boolean found = false;
      for (;iterator.hasNext(); unit = iterator.next()) {
         if (unit.interval.inside(Itime)) {
            result = unit.getValue(Itime);
         }
      }
      
      if (found) return result;
      else return null;
   }
   
   public void addUnit(UnitObject<T> unit) throws IntervalOverlapException {
      unitList.add(unit);
      mergeAndSort();
   }
 
   public String toString() {
/*
      StringBuffer sb = new StringBuffer("");
      sb.append("Moving object" + "(" + unitList.size() +")(");
      UnitObject<T> unit = null;
      for (ListIterator<UnitObject<T>> I = unitList.listIterator(); I.hasNext(); unit = I.next()) {
         if (unit == null) {
            System.out.println("Null unit!");
            break;
         }
         sb.append(unit.toString() + ", ");
      }
      sb.append(")");
      return sb.toString();
*/
      return "Moving object(" + unitList.toString() + ")";
   }

   public static void main(String[] args) {
      System.out.println("Testing!");
      MInteger mint = new MInteger(10);
      MPoint mpoint = new MPoint(5, 5);
      MovingObject<Integer> movingint = new MovingObject<Integer>();
      MovingObject<MPoint> movingpoint1 = new MovingObject<MPoint>();
      MovingObject<MPoint> movingpoint2 = new MovingObject<MPoint>();
      MovingObject<Float> mfloat = null;
      DistancePP dist = new DistancePP();
      try {
         movingint.addUnit(new UnitObject<Integer>(new TimeInterval(0, 10), new Integer(5)));
         movingint.addUnit(new UnitObject<Integer>(new TimeInterval(10, 20), new Integer(10)));
         movingpoint1.addUnit(new UnitObject<MPoint>(new TimeInterval(0, 15), new MPoint(5,6)));
         movingpoint1.addUnit(new UnitObject<MPoint>(new TimeInterval(15, 20), new MPoint(6,7)));
         movingpoint1.addUnit(new UnitObject<MPoint>(new TimeInterval(20, 30), new MPoint(7,9)));
         movingpoint2.addUnit(new UnitObject<MPoint>(new TimeInterval(5, 10), new MPoint(0,0)));
         movingpoint2.addUnit(new UnitObject<MPoint>(new TimeInterval(10, 25), new MPoint(2,4)));
         movingpoint2.addUnit(new UnitObject<MPoint>(new TimeInterval(25, 35), new MPoint(4,8)));
//         mfloat = Operations.Distance(movingpoint1, movingpoint2);
         mfloat = dist.execute(movingpoint1, movingpoint2);
      }
      catch (IntervalOverlapException e) {
         System.out.println("Intervali se preklapaju!");
      }
      catch (IllegalArgumentException e) {
         System.out.println("Illegal arguments!");
      }
      
      System.out.println("Integer: " + mint);
      System.out.println("Point: " + mpoint);
      System.out.println("Moving int: " + movingint);
      System.out.println("Moving point 1: " + movingpoint1);
      System.out.println("Moving point 2: " + movingpoint2);
      System.out.println("Moving distance 2: " + mfloat);
   }   
}


