/*
 * Created on 19. svi. 2010.
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

public interface Movable {

   public static final int TYPE_UNDEFINED = 0;
   public static final int MOVABLE_INT = 1;
   public static final int MOVABLE_FLOAT = 2;
   public static final int MOVABLE_BOOLEAN = 3;
   public static final int MOVABLE_STRING = 4;
   public static final int MOVABLE_POINT = 5;
   public static final int MOVABLE_LINESTRING = 6;
   public static final int MOVING_INT = 7;
   public static final int MOVING_FLOAT = 8;
   public static final int MOVING_BOOLEAN = 9;
   public static final int MOVING_STRING = 10;
   public static final int MOVING_POINT = 11;
   public static final int MOVING_LINESTRING = 12;
   
   public final int type = 0;
   public Movable getValue(TimeInstant t);
   
}
