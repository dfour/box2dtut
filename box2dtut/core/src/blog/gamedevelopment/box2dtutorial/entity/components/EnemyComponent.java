package blog.gamedevelopment.box2dtutorial.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class EnemyComponent implements Component, Poolable{
	
	public static enum Type { DROPLET, CLOUD };
	
	public boolean isDead = false;
	public float xPosCenter = -1;
	public boolean isGoingLeft = false;
	public float shootDelay = 2f;
	public float timeSinceLastShot = 0f;
	public Type enemyType = Type.DROPLET;
	@Override
	public void reset() {
		shootDelay = 2f;
		timeSinceLastShot = 0f;
		enemyType = Type.DROPLET;
		isDead = false;
		xPosCenter = -1;
		isGoingLeft = false;
	}

}
