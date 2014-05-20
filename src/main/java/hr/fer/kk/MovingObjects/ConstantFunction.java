/*
 * Created on 16. kol. 2010.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

/*
 * There is only one class representing constant functions, because
 * implementation is rather simple. For linear functions there must
 * be one implementation for each data type.
 */
public class ConstantFunction<T> implements IFunction<T> {
   public T value = null;

   public T getValue(TimeInstant Itime) {
      // TODO Auto-generated method stub
      return value;
   }
   
   public ConstantFunction(T t_value) {
      value = t_value;
   }

   public void setValue(T t_value) {
      value = t_value;
   }
   
   public String toString() {
      return "CFun(" + value + ")";
   }
}
