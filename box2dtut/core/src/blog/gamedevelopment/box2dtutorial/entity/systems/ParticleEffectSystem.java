package blog.gamedevelopment.box2dtutorial.entity.systems;

import blog.gamedevelopment.box2dtutorial.entity.components.Mapper;
import blog.gamedevelopment.box2dtutorial.entity.components.ParticleEffectComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class ParticleEffectSystem extends IteratingSystem{

	private static final boolean shouldRender = true;
	
	private Array<Entity> renderQueue;
	private SpriteBatch batch;
	private OrthographicCamera camera;
		

	@SuppressWarnings("unchecked")
	public ParticleEffectSystem(SpriteBatch sb, OrthographicCamera cam) {
		super(Family.all(ParticleEffectComponent.class).get());
		renderQueue = new Array<Entity>();
		batch = sb;
		camera = cam;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		batch.setProjectionMatrix(camera.combined);
        batch.enableBlending();
        // Render PE
        if(shouldRender){
	        batch.begin();
	        for (Entity entity : renderQueue) {
	        	ParticleEffectComponent pec = Mapper.peCom.get(entity);
	            pec.particleEffect.draw(batch,deltaTime);
	        }
	        batch.end();
        }
        renderQueue.clear();
	}


	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ParticleEffectComponent pec = Mapper.peCom.get(entity);
		if(pec.isDead){
			pec.timeTilDeath -= deltaTime;
		}
		
		 // Move PE if attached
		if(pec.isattached){
			pec.particleEffect.setPosition(
					pec.attachedBody.getPosition().x + pec.xOffset,
					pec.attachedBody.getPosition().y + pec.yOffset);
		}
		 // free PE if completed
		if(pec.particleEffect.isComplete() || pec.timeTilDeath <= 0){
			getEngine().removeEntity(entity);
		}else{
			renderQueue.add(entity);
		}
	}
}
