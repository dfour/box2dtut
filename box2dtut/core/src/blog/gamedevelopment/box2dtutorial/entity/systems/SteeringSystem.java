package blog.gamedevelopment.box2dtutorial.entity.systems;

import blog.gamedevelopment.box2dtutorial.entity.components.Mapper;
import blog.gamedevelopment.box2dtutorial.entity.components.SteeringComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.GdxAI;

public class SteeringSystem extends IteratingSystem {

	@SuppressWarnings("unchecked")
	public SteeringSystem() {
		super(Family.all(SteeringComponent.class).get());
	}
	
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		GdxAI.getTimepiece().update(deltaTime);
	}


	@Override
	protected void processEntity(Entity entity, float deltaTime) {		
		SteeringComponent steer = Mapper.sCom.get(entity);
        steer.update(deltaTime);
	}
}
