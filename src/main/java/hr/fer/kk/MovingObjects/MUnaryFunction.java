/*
 * Created on 21. svi. 2010.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

import java.util.Iterator;

public abstract class MUnaryFunction<R, T> extends MFunction {

   public abstract R execute(T arg1);

   /*
    * A default implementation of execute for UnitObjects, takes into account
    * only the starting value in the interval
    * This can be overridden for speccific operations to modify their behaviour
    */
   public UnitObject<R> execute(UnitObject<T> arg1) {
      TimeInterval interval = new TimeInterval(arg1.interval.start, arg1.interval.end);
      UnitObject<R> unit = new UnitObject<R>(interval, execute(arg1.getValue(arg1.interval.start)));
      return null;
   }
   
   public MovingObject<R> execute(MovingObject<T> arg1) throws IntervalOverlapException {
      MovingObject<R> retobj = new MovingObject<R>();
      Iterator<UnitObject<T>> it = arg1.getIterator();
      UnitObject<T> unit1 = null;
      UnitObject<R> unit2 = null;
      for (;it.hasNext(); unit1 = it.next()) {
         unit2 = execute(unit1);
         retobj.addUnit(unit2);
      }
      return retobj;
   }
   
   protected void checkArguments() throws IllegalArgumentException {      
   }

}

