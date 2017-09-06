package blog.gamedevelopment.box2dtutorial.views;

import blog.gamedevelopment.box2dtutorial.Box2DTutorial;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoadingScreen implements Screen {
	private Box2DTutorial parent;
	private TextureAtlas atlas;
	private AtlasRegion title;
	private Animation flameAnimation;
	
	public final int IMAGE = 0;		// loading images
	public final int FONT = 1;		// loading fonts
	public final int PARTY = 2;		// loading particle effects
	public final int SOUND = 3;		// loading sounds
	public final int MUSIC = 4;		// loading music
	
	private int currentLoadingStage = 0;
	
	// timer for exiting loading screen
	public float countDown = 0.1f;
	private AtlasRegion dash;
	private Stage stage;
	private Table table;
	private Image titleImage;
	private AtlasRegion copyright;
	private Image copyrightImage;
	private Table loadingTable;
	private AtlasRegion background;
	
	
	public LoadingScreen(Box2DTutorial box2dTutorial){
		parent = box2dTutorial;
		stage = new Stage(new ScreenViewport());
		
		loadAssets();
		// initiate queueing of images but don't start loading
		parent.assMan.queueAddImages();
		System.out.println("Loading images....");		
	}
	
	private void loadAssets() {
		// load loading images and wait until finished
		parent.assMan.queueAddLoadingImages();
		parent.assMan.manager.finishLoading();
				
		// get images used to display loading progress
		atlas = parent.assMan.manager.get("images/loading.atlas");
		title = atlas.findRegion("staying-alight-logo");
		dash = atlas.findRegion("loading-dash");
		background = atlas.findRegion("flamebackground");
		copyright = atlas.findRegion("copyright");
		flameAnimation = new Animation(0.07f, atlas.findRegions("flames/flames"), PlayMode.LOOP);
		
	}

	@Override
	public void show() {
		
		titleImage = new Image(title);
		copyrightImage = new Image(copyright);

		table = new Table();
		table.setFillParent(true);
		table.setDebug(false);
		table.setBackground(new TiledDrawable(background));
		
		loadingTable = new Table();
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		loadingTable.add(new LoadingBarPart(dash,flameAnimation));
		
		
		table.add(titleImage).align(Align.center).pad(10, 0, 0, 0).colspan(10); 
		table.row(); // move to next row
		table.add(loadingTable).width(400);
		table.row();
		table.add(copyrightImage).align(Align.center).pad(200, 0, 0, 0).colspan(10);
		
		stage.addActor(table);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    	
		if (parent.assMan.manager.update()) { // Load some, will return true if done loading
			currentLoadingStage+= 1;
			if(currentLoadingStage <= 5){
				loadingTable.getCells().get((currentLoadingStage-1)*2).getActor().setVisible(true);
				loadingTable.getCells().get((currentLoadingStage-1)*2+1).getActor().setVisible(true);
			}
            switch(currentLoadingStage){
            case FONT:
            	System.out.println("Loading fonts....");
            	parent.assMan.queueAddFonts();
            	break;
            case PARTY:	
            	System.out.println("Loading Particle Effects....");
            	parent.assMan.queueAddParticleEffects();
            	break;
            case SOUND:
            	System.out.println("Loading Sounds....");
            	parent.assMan.queueAddSounds();
            	break;
            case MUSIC:
            	System.out.println("Loading fonts....");
            	parent.assMan.queueAddMusic();
            	break;
            case 5:	
            	System.out.println("Finished");
            	break;
            }
	    	if (currentLoadingStage >5){
	    		countDown -= delta;
	    		currentLoadingStage = 5;
	    		if(countDown < 0){
	    			parent.changeScreen(Box2DTutorial.MENU);
	    		}
            }
        }
		
		stage.act();
		//stage.draw();
	}

	@Override
	public void resize(int width, int height) {		
		stage.getViewport().update(width, height,true);
	}

	@Override
	public void pause() {		
	}

	@Override
	public void resume() {		
	}

	@Override
	public void hide() {		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	class LoadingBarPart extends Actor{
		
		private AtlasRegion image = dash;
		private Animation flameAnimation;
		private float stateTime = 0f; 
		private TextureRegion currentFrame;

		
		public LoadingBarPart(AtlasRegion ar, Animation an) {
			super();
			image = ar;
			flameAnimation = an;
			this.setWidth(30);
			this.setHeight(25);
			this.setVisible(false);
			//this.debug();
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			super.draw(batch, parentAlpha);
			batch.draw(image, getX(),getY(), 30, 30);
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			batch.draw(currentFrame, getX()-5,getY(), 40, 40);
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		}

		@Override
		public void act(float delta) {
			super.act(delta);
			stateTime += delta; // Accumulate elapsed animation time
	        currentFrame = flameAnimation.getKeyFrame(stateTime, true);
		}
		
	}
}
