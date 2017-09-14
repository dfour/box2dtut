package blog.gamedevelopment.box2dtutorial.entity.systems;

import blog.gamedevelopment.box2dtutorial.DFUtils;
import blog.gamedevelopment.box2dtutorial.LevelFactory;
import blog.gamedevelopment.box2dtutorial.ai.SteeringPresets;
import blog.gamedevelopment.box2dtutorial.entity.components.B2dBodyComponent;
import blog.gamedevelopment.box2dtutorial.entity.components.BulletComponent;
import blog.gamedevelopment.box2dtutorial.entity.components.EnemyComponent;
import blog.gamedevelopment.box2dtutorial.entity.components.EnemyComponent.Type;
import blog.gamedevelopment.box2dtutorial.entity.components.Mapper;
import blog.gamedevelopment.box2dtutorial.entity.components.SteeringComponent;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

public class EnemySystem extends IteratingSystem{

	private ComponentMapper<EnemyComponent> em;
	private ComponentMapper<B2dBodyComponent> bodm;
	private LevelFactory levelFactory;

	
	@SuppressWarnings("unchecked")
	public EnemySystem(LevelFactory lvlf){
		super(Family.all(EnemyComponent.class).get());
		em = ComponentMapper.getFor(EnemyComponent.class);
		bodm = ComponentMapper.getFor(B2dBodyComponent.class);
		levelFactory = lvlf;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		EnemyComponent enemyCom = em.get(entity);		// get EnemyComponent
		B2dBodyComponent bodyCom = bodm.get(entity);	// get B2dBodyComponent
		
		if(enemyCom.enemyType == Type.DROPLET){
			// get distance of enemy from its original start position (pad center)
			float distFromOrig = Math.abs(enemyCom.xPosCenter - bodyCom.body.getPosition().x);
			
			// if distance > 1 swap direction
			enemyCom.isGoingLeft = (distFromOrig > 1)? !enemyCom.isGoingLeft:enemyCom.isGoingLeft;
			
			// set speed base on direction
			float speed = enemyCom.isGoingLeft?-0.01f:0.01f;
			
			// apply speed to body
			bodyCom.body.setTransform(bodyCom.body.getPosition().x + speed,
					bodyCom.body.getPosition().y, 
					bodyCom.body.getAngle());	
		}else if(enemyCom.enemyType == Type.CLOUD){
			B2dBodyComponent b2Player = Mapper.b2dCom.get(levelFactory.player);
			B2dBodyComponent b2Enemy = Mapper.b2dCom.get(entity);
			
			float distance = b2Player.body.getPosition().dst(b2Enemy.body.getPosition());
			//System.out.println(distance);
			SteeringComponent scom = Mapper.sCom.get(entity);
			if(distance < 3 && scom.currentMode != SteeringComponent.SteeringState.FLEE){
				scom.steeringBehavior = SteeringPresets.getFlee(Mapper.sCom.get(entity),Mapper.sCom.get(levelFactory.player));
				scom.currentMode = SteeringComponent.SteeringState.FLEE;
			}else if(distance > 3 && distance < 10 && scom.currentMode != SteeringComponent.SteeringState.ARRIVE){
				scom.steeringBehavior = SteeringPresets.getArrive(Mapper.sCom.get(entity),Mapper.sCom.get(levelFactory.player));
				scom.currentMode = SteeringComponent.SteeringState.ARRIVE;
			}else if(distance > 15 && scom.currentMode != SteeringComponent.SteeringState.WANDER){
				scom.steeringBehavior  = SteeringPresets.getWander(Mapper.sCom.get(entity));
				scom.currentMode = SteeringComponent.SteeringState.WANDER;
			}
			
			// should enemy shoot
			if(scom.currentMode == SteeringComponent.SteeringState.ARRIVE){
				// enemy is following
				if(enemyCom.timeSinceLastShot >= enemyCom.shootDelay){
					//do shoot
					Vector2 aim = DFUtils.aimTo(bodyCom.body.getPosition(), b2Player.body.getPosition());
					aim.scl(3);
					levelFactory.createBullet(bodyCom.body.getPosition().x, 
							bodyCom.body.getPosition().y, 
							aim.x, 
							aim.y,
							BulletComponent.Owner.ENEMY);
					//reset timer
					enemyCom.timeSinceLastShot = 0;
				}
			}
		}
		
		// do shoot timer
		enemyCom.timeSinceLastShot += deltaTime;
		
		// check for dead enemies
		if(enemyCom.isDead){
			bodyCom.isDead =true;
		}
	}
}
