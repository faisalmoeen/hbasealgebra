package de.tuberlin.dima.hbasealgebra.operators;

import de.tuberlin.dima.hbasealgebra.datatypes.Real;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Line;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Point;
import de.tuberlin.dima.hbasealgebra.temporal.datatypes.Instant;
import de.tuberlin.dima.hbasealgebra.temporal.datatypes.Periods;
import de.tuberlin.dima.hbasealgebra.temporal.datatypes.iPoint;
import de.tuberlin.dima.hbasealgebra.temporal.datatypes.mBool;
import de.tuberlin.dima.hbasealgebra.temporal.datatypes.mPoint;
import de.tuberlin.dima.hbasealgebra.temporal.datatypes.mReal;

public class Operators {
	
	//Extracts the point from the (instant , point) pair.
	public Point val(iPoint ipoint)
	{
		if(ipoint==null){
			return null;
		}
		else 
			return ipoint.getPoint();
	}
	
	//Creates an instant from its textual representation.
	public Instant createInstant(String text, String pattern)
	{
		return new Instant(text, pattern);
	}
	
	//Checks whether the given instant is within the mpoint’s definition time.
	public boolean present(mPoint mpoint, Instant instant)
	{
		return false;
	}
	
	//Projects the complete mpoint into the 2D space.
	public Line trajectory(mPoint mpoint)
	{
		return mpoint.getTrajectory();
	}
	
	//Extract the instant (timestamp) from the (instant , point) pair.
	public Instant inst(iPoint ipoint)
	{
		return ipoint.getInstant();
	}
	
	//Computes the spatio-temporal intersection of its arguments. 
	//The result is the	mpoint with the common history of both arguments.
	public mPoint intersection(mPoint mpoint1, mPoint mpoint2)
	{
		return null;
	}
	
	//Computes the driving distance of the moving point.
	public Real length(mPoint mpoint)
	{
		return null;
	}
	
	//Returns the set of all time intervals where
	//the mpoint is defined (i.e. the time covered by it’s history).
	public Periods defTime(mPoint mpoint)
	{
		return null;
	}
	
	//Returns ‘TRUE’, if the mbool (a temporal boolean function) ever becomes ‘TRUE’.
	public boolean sometimes(mBool mbool)
	{
		return false;
	}
	
	//Returns a temporal boolean function with the same definition time as the first
	//argument, yielding ‘TRUE’ for periods, where the first argument is less than or
	//equal to the second, and ‘FALSE’ where it is greater.
	public mBool isLessThan(mReal mreal, Real real)
	{
		return null;
	}
	
	//Extract the current position of mpoint at	the given instant .
	public iPoint atInstant(mPoint mpoint, Instant instant)
	{
		return null;
	}
	
	//Tests, whether the mpoint ever passes the	second argument.
	public boolean passes(mPoint mpoint, Point point)
	{
		return false;
	}
	
	//Computes the minimum distance between the arguments.
	public Real distance(Line line1, Line line2)
	{
		return null;
	}
	
	//Computes the minimum distance between the arguments.
	public mReal distance(mPoint mpoint1, mPoint mpoint2)
	{
		return null;
	}
	
	//Computes the initial position of the argument.
	public iPoint initial(mPoint mpoint)
	{
		return null;
	}
	
	//Restricts the first argument to the temporal intervals, where its projection is 
	//included by/equal to the second argument.
	public mBool at(mBool mbool, boolean bool)
	{
		return null;
	}
	
	//Restricts the first argument to the temporal intervals, where its projection is 
	//included by/equal to the second argument.
	public mPoint at(mPoint mpoint, Point point)
	{
		return null;
	}
	
	//Restricts mpoint’s history to the given time intervals.
	public mPoint atPeriods(mPoint mpoint, Periods periods)
	{
		return null;
	}
	
	//Concatenates the histories of two moving points to a common one. Both arguments
	//must have distinct definition times.
	public mPoint concat(mPoint mpoint1, mPoint mpoint2)
	{
		return null;
	}

	//Returns ‘TRUE’, if the argument is undefined or empty (for a moving type or
	//periods value X ).
	public boolean isEmpty(Object obj)
	{
		return true;
	}
}
