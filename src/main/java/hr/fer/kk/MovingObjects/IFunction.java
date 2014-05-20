
package hr.fer.kk.MovingObjects;

/*
 * As oposed to class MFunction and those derived from it, this interface
 * represents operations that do not operate on moving objects.
 * This interface represents functions used to describe a a moving object on
 * a single unit.
 */
public interface IFunction <T> {
   public abstract T getValue(TimeInstant Itime);
}
