package blog.gamedevelopment.box2dtutorial.ai;

import blog.gamedevelopment.box2dtutorial.entity.components.SteeringComponent;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class SteeringPresets {

	public static Wander<Vector2> getWander(SteeringComponent scom){
		Wander<Vector2> wander = new Wander<Vector2>(scom)
				.setFaceEnabled(false) // let wander behaviour manage facing
				.setWanderOffset(5f) // distance away from entity to set target
				.setWanderOrientation(180f) // the initial orientation
				.setWanderRadius(10f) // size of target 
				.setWanderRate(MathUtils.PI2 * 4); // higher values = more spinning
		return wander;
	}
	
	public static Seek<Vector2> getSeek(SteeringComponent seeker, SteeringComponent target){
		Seek<Vector2> seek = new Seek<Vector2>(seeker,target);
		return seek;
	}
	
	public static Flee<Vector2> getFlee(SteeringComponent runner, SteeringComponent fleeingFrom){
		Flee<Vector2> seek = new Flee<Vector2>(runner,fleeingFrom);
		return seek;
	}
	
	public static Arrive<Vector2> getArrive(SteeringComponent runner, SteeringComponent target){
		Arrive<Vector2> arrive = new Arrive<Vector2>(runner, target)
				.setTimeToTarget(0.1f) // default 0.1f
				.setArrivalTolerance(7f) //
				.setDecelerationRadius(10f);
				
		return arrive;
	}
}
