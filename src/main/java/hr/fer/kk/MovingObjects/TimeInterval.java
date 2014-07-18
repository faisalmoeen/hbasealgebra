/*
 * Created on 19. svi. 2010.
 *
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package hr.fer.kk.MovingObjects;

/*
 * Starting and ending point od the interval can only be equal to or greater then zero.
 * A value -1 is also allower meaning an infinite value. For starting point its -INF and 
 * for ending point its +INF
 */
public class TimeInterval {

   public float start;
   public float end;

   
   public TimeInterval(float _start, float _end) {
      this.start = _start;
      this.end = _end;
   }
   
   public TimeInterval(TimeInstant _Istart, TimeInstant _Iend) {
      this.start = _Istart.time;
      this.end = _Iend.time;
   }

   public TimeInstant getStart() {
      return new TimeInstant(start);
   }
   
   public TimeInstant getEnd() {
      return new TimeInstant(end);
   }
   
   public float length() {
      return end - start;
   }
   
   public String toString() {
      String sstart, send;
      if (start == -1) sstart = "-INF";
      else sstart = Float.toString(start);
      if (end == -1) send = "+INF";
      else send = Float.toString(end);
      return "Interval[" + sstart + ", " + send + "]";
   }
   
   public boolean inside(float time) {
      if (time >= start && time <= end) return true;
      else return false;
   }
   
   public boolean inside(TimeInstant Itime) {
      return inside(Itime.time);
   }
   
   /*
    * It is assumed that intervals are valid, that end of each interval is after start
    */
   public boolean overlaps(TimeInterval _interval) {
      if (end <= _interval.start || start >= _interval.end) return false;
      return true;
   }
}
