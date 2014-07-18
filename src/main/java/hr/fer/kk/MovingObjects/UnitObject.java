/*
 * Created on 19. svi. 2010.
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

public class UnitObject<T> implements Comparable<UnitObject<T>> {

   public TimeInterval interval;
   public IFunction<T> fun;
   // public T value; // OBSOLETE 

   public UnitObject(TimeInterval _interval, T _value) {
      this.interval = _interval;
      // this.value = _value;
      fun = new ConstantFunction<T>(_value);
   }

   public UnitObject(TimeInterval _interval, IFunction<T> _fun) {
      this.interval = _interval;
      this.fun = _fun;
   }
   
   public TimeInterval getInterval() {
      return interval;
   }
   
   /*
    * This might not be neccessary or even desirable
    */
   /*
   public T getValue() {
      return value;
   }
   */

   public T getValue(TimeInstant Itime) {
      if (interval.inside(Itime)) return fun.getValue(Itime);
      else return null;
   }

   /*
    * A default getValue function
    * this can be overridden to modify behaviour
    */
   public T getValue(float time) {
      return fun.getValue(new TimeInstant(time));
   }
   
   /*
    * A default getValue function this can be overridden to modify behaviour.
    * This function is only useful for constant unit objects.
    */
   public T getValue() {
      return getValue(interval.start);
   }
   
   public String toString() {
      return "Unit(" + interval.toString() + "," + fun.toString() + ")";
   }

   /*
    * Method that merges two unit elements if possible. Result is stored in this element,
    * second element remains unchanged. 
    * It is assumed that the interval of the second element comes
    * after the interval of the first element.
    * 
    * Note: should maybe return boolean, true if merge actually happened 
    */
   public void merge(UnitObject<T> unit) {
      // placeholder
   }


   /*
    * Compares only the starting value of the interval
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   public int compareTo(UnitObject<T> o) {
      if (interval.start < o.interval.start) return -1;
      else if (interval.start > o.interval.start) return 1;
      else return 0;
   }
}
