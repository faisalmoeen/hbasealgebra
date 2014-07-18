/*
 * Created on 3. ruj. 2010.
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

/*
 * Through this interface I try to generalize objects that can use linear function
 * on units. Top be able to automate this, the objects need to support addition
 * and multiplication by scalar. 
 */
public interface ILinearizable {
    public void addOpr(ILinearizable el2);
    public void mulBySc(float scalar);
}
