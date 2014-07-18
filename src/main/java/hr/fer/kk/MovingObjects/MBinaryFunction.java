/*
 * Created on 21. svi. 2010.
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

import java.util.Iterator;

public abstract class MBinaryFunction<R, T1, T2> extends MFunction {

   public abstract R execute(T1 arg1, T2 arg2) throws IllegalArgumentException;

   /*
    * A default implementation of execute for UnitObjects, takes into account
    * only the starting value in the intervals
    * This can be overridden for speccific operations to modify their behaviour
    */
   public UnitObject<R> execute(UnitObject<T1> arg1, UnitObject<T2> arg2)
   throws IllegalArgumentException, IntervalOverlapException {
      TimeInterval int1 = new TimeInterval(arg1.interval.start, arg1.interval.end);
      TimeInterval int2 = new TimeInterval(arg2.interval.start, arg2.interval.end);
      float rstart, rend;

      if (int1.start > int2.end || int1.end < int2.start) {
         throw new IntervalOverlapException();
      }
      rstart = (int1.start > int2.start) ? int1.start : int2.start;
      rend = (int1.end > int2.end) ? int2.end : int1.end;
      TimeInterval int3 = new TimeInterval(rstart, rend);

      UnitObject<R> unit = new UnitObject<R>(int3, execute(arg1.getValue(arg1.interval.start)
                                                         , arg2.getValue(arg2.interval.start)));
      return unit;
   }

   public MovingObject<R> execute(MovingObject<T1> arg1, T2 arg2) throws IllegalArgumentException, IntervalOverlapException {
      execute(arg1, new MovingObject<T2>(arg2));
      return null;
   }

   public MovingObject<R> execute(T1 arg1, MovingObject<T2> arg2) throws IllegalArgumentException, IntervalOverlapException {
      execute(new MovingObject<T1>(arg1), arg2);
      return null;
   }

   public MovingObject<R> execute(MovingObject<T1> arg1, MovingObject<T2> arg2) throws IllegalArgumentException, IntervalOverlapException {
      MovingObject<R> retobj = new MovingObject<R>();
      Iterator<UnitObject<T1>> it1 = arg1.getIterator();
      Iterator<UnitObject<T2>> it2 = arg2.getIterator();
      UnitObject<T1> unit1 = null;
      UnitObject<T2> unit2 = null;
      UnitObject<R> unitR = null;
      TimeInterval tint1, tint2;

      if (it1.hasNext() && it2.hasNext()) {
         unit1 = it1.next();
         unit2 = it2.next();
         tint1 = unit1.interval;
         tint2 = unit2.interval;
      }
      else {
         return retobj;
      }

      while (unit1 != null && unit2 != null) {
         if (tint1.end < tint2.start) {    // disjoined intervals
            if (!it1.hasNext()) return retobj;
            unit1 = it1.next();
            tint1 = unit1.interval;
         }
         else if (tint2.end < tint1.start) {   // disjoined intervals
            if (!it2.hasNext()) return retobj;
            unit2 = it2.next();
            tint2 = unit2.interval;
         } 
         // interval2 within interval1
         else if (tint2.start >= tint1.start && tint2.end <= tint1.end){
            unitR = execute(unit1, unit2);
            retobj.addUnit(unitR);
            if (!it2.hasNext()) return retobj;
            unit2 = it2.next();
            tint2 = unit2.interval;
         }
         // interval1 within interval2
         else if (tint1.start >= tint2.start && tint1.end <= tint2.end){
            unitR = execute(unit1, unit2);
            retobj.addUnit(unitR);
            if (!it1.hasNext()) return retobj;
            unit1 = it1.next();
            tint1 = unit1.interval;
         }
         // interval1 starts before interval2
         else if (tint1.start <= tint2.start && tint1.end > tint2.start) {
            unitR = execute(unit1, unit2);
            retobj.addUnit(unitR);
            if (!it1.hasNext()) return retobj;
            unit1 = it1.next();
            tint1 = unit1.interval;
         }
         // interval2 starts before interval1
         else if (tint2.start <= tint1.start && tint2.end > tint1.start) {
            unitR = execute(unit1, unit2);
            retobj.addUnit(unitR);
            if (!it2.hasNext()) return retobj;
            unit2 = it2.next();
            tint2 = unit2.interval;
         }
         // this shouldnt happen because all situations are covered before
         else {
            throw new IntervalOverlapException();
         }
      }
      
      return retobj;
   }


   protected void checkArguments() throws IllegalArgumentException {      
   }
}
