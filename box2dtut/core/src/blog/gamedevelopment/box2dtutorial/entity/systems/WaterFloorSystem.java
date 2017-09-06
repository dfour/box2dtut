package blog.gamedevelopment.box2dtutorial.entity.systems;

import blog.gamedevelopment.box2dtutorial.entity.components.B2dBodyComponent;
import blog.gamedevelopment.box2dtutorial.entity.components.WaterFloorComponent;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;

public class WaterFloorSystem extends IteratingSystem {
	private Entity player;
	private ComponentMapper<B2dBodyComponent> bm = ComponentMapper.getFor(B2dBodyComponent.class);

	@SuppressWarnings("unchecked")
	public WaterFloorSystem(Entity player) {
		super(Family.all(WaterFloorComponent.class).get());
		this.player = player;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		// get current y level of player entity
		float currentyLevel = player.getComponent(B2dBodyComponent.class).body.getPosition().y;
		// get the body component of the wall we're updating
		Body bod = bm.get(entity).body;
		
		float speed = (currentyLevel / 1000);
		
		speed = speed>0.25f?0.25f:speed;
		
		// make sure water doesn't get too far behind
		if(bod.getPosition().y < currentyLevel - 50){
			bod.setTransform(bod.getPosition().x, currentyLevel - 50, bod.getAngle());
		}
		
		
		bod.setTransform(bod.getPosition().x, bod.getPosition().y+speed, bod.getAngle());
	}

}
