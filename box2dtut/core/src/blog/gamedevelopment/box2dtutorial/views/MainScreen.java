package blog.gamedevelopment.box2dtutorial.views;

import blog.gamedevelopment.box2dtutorial.Box2DTutorial;
import blog.gamedevelopment.box2dtutorial.DFUtils;
import blog.gamedevelopment.box2dtutorial.LevelFactory;
import blog.gamedevelopment.box2dtutorial.controller.KeyboardController;
import blog.gamedevelopment.box2dtutorial.entity.components.Mapper;
import blog.gamedevelopment.box2dtutorial.entity.components.PlayerComponent;
import blog.gamedevelopment.box2dtutorial.entity.systems.AnimationSystem;
import blog.gamedevelopment.box2dtutorial.entity.systems.BulletSystem;
import blog.gamedevelopment.box2dtutorial.entity.systems.CollisionSystem;
import blog.gamedevelopment.box2dtutorial.entity.systems.EnemySystem;
import blog.gamedevelopment.box2dtutorial.entity.systems.LevelGenerationSystem;
import blog.gamedevelopment.box2dtutorial.entity.systems.ParticleEffectSystem;
import blog.gamedevelopment.box2dtutorial.entity.systems.PhysicsDebugSystem;
import blog.gamedevelopment.box2dtutorial.entity.systems.PhysicsSystem;
import blog.gamedevelopment.box2dtutorial.entity.systems.PlayerControlSystem;
import blog.gamedevelopment.box2dtutorial.entity.systems.RenderingSystem;
import blog.gamedevelopment.box2dtutorial.entity.systems.SteeringSystem;
import blog.gamedevelopment.box2dtutorial.entity.systems.WallSystem;
import blog.gamedevelopment.box2dtutorial.entity.systems.WaterFloorSystem;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainScreen implements Screen {
	private Box2DTutorial parent;
	private OrthographicCamera cam;
	private KeyboardController controller;
	private SpriteBatch sb;
	private PooledEngine engine;
	private LevelFactory lvlFactory;
	
	//private Sound ping;
	//private Sound boing;
	private Entity player;	

	
	/**
	 * @param box2dTutorial
	 */
	public MainScreen(Box2DTutorial box2dTutorial) {
		parent = box2dTutorial;
		//parent.assMan.queueAddSounds();
		//parent.assMan.manager.finishLoading();
		//ping = parent.assMan.manager.get("sounds/ping.wav",Sound.class);
		//boing = parent.assMan.manager.get("sounds/boing.wav",Sound.class);
		controller = new KeyboardController();
		engine = new PooledEngine();
		// next guide - changed this to atlas
		lvlFactory = new LevelFactory(engine,parent.assMan);
		
		
		sb = new SpriteBatch();
		RenderingSystem renderingSystem = new RenderingSystem(sb);
		cam = renderingSystem.getCamera();
		ParticleEffectSystem particleSystem = new ParticleEffectSystem(sb,cam);
		sb.setProjectionMatrix(cam.combined);
		
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new PhysicsSystem(lvlFactory.world));
        engine.addSystem(renderingSystem);
        // not a fan of splitting batch into rendering and particles but I like the separation of the systems
        engine.addSystem(particleSystem); // particle get drawns on top so should be placed after normal rendering
        engine.addSystem(new PhysicsDebugSystem(lvlFactory.world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new SteeringSystem());
        engine.addSystem(new PlayerControlSystem(controller,lvlFactory));
        player = lvlFactory.createPlayer(cam);
        engine.addSystem(new EnemySystem(lvlFactory));
        engine.addSystem(new WallSystem(player));
        engine.addSystem(new WaterFloorSystem(player));
        engine.addSystem(new BulletSystem(player));
        engine.addSystem(new LevelGenerationSystem(lvlFactory));
         
        
        lvlFactory.createFloor();
        lvlFactory.createWaterFloor();
        //lvlFactory.createSeeker(Mapper.sCom.get(player),20,15);
        
        
        int wallWidth = (int) (1*RenderingSystem.PPM);
        int wallHeight = (int) (60*RenderingSystem.PPM);
        TextureRegion wallRegion = DFUtils.makeTextureRegion(wallWidth, wallHeight, "222222FF");
        lvlFactory.createWalls(wallRegion); //TODO make some damn images for this stuff  
	}
	

	@Override
	public void show() {
		Gdx.input.setInputProcessor(controller);	
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		engine.update(delta);
		
		//check if player is dead. if so show end screen
		PlayerComponent pc = (player.getComponent(PlayerComponent.class));
		if(pc.isDead){
			DFUtils.log("YOU DIED : back to menu you go!");
			parent.lastScore = (int) pc.cam.position.y;
			parent.changeScreen(Box2DTutorial.ENDGAME);	
		}

	}

	@Override
	public void resize(int width, int height) {		
	}

	@Override
	public void pause() {		
	}

	@Override
	public void resume() {	
		Gdx.input.setInputProcessor(controller);	
	}

	@Override
	public void hide() {		
	}

	@Override
	public void dispose() {
		sb.dispose();
		engine.clearPools();
	}

}
