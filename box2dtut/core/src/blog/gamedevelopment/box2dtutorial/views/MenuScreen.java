package blog.gamedevelopment.box2dtutorial.views;

import blog.gamedevelopment.box2dtutorial.Box2DTutorial;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen{
	
	private Box2DTutorial parent;
	private Stage stage;
	private Skin skin;
	private TextureAtlas atlas;
	private AtlasRegion background;
	
	public MenuScreen(Box2DTutorial box2dTutorial){
		parent = box2dTutorial;
		/// create stage and set it as input processor
		stage = new Stage(new ScreenViewport());
		
		parent.assMan.queueAddSkin();
		parent.assMan.manager.finishLoading();
		skin = parent.assMan.manager.get("skin/glassy-ui.json");
		atlas = parent.assMan.manager.get("images/loading.atlas");
		background = atlas.findRegion("flamebackground");
		
	}

	@Override
	public void show() {
		// debug to skip menu
		//DFUtils.log("To the game");
		//parent.changeScreen(Box2DTutorial.APPLICATION);
		
		
		Gdx.input.setInputProcessor(stage); 
		// Create a table that fills the screen. Everything else will go inside this table.
		Table table = new Table();
		table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);
        
    	table.setBackground(new TiledDrawable(background));
        
        
        //create buttons
        TextButton newGame = new TextButton("New Game", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);
        
        //add buttons to table
        table.add(newGame).fillX().uniformX();
		table.row().pad(10, 0, 10, 0);
		table.add(preferences).fillX().uniformX();
		table.row();
		table.add(exit).fillX().uniformX();
		
		// create button listeners
		exit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();				
			}
		});
		
		newGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(Box2DTutorial.APPLICATION);			
			}
		});
		
		preferences.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(Box2DTutorial.PREFERENCES);					
			}
		});
		
	}

	@Override
	public void render(float delta) {
		// clear the screen ready for next set of images to be drawn
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// tell our stage to do actions and draw itself
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		
		// temp debug stuff(ignore menu  go straight to play..saves time)
		//parent.changeScreen(Box2DTutorial.APPLICATION);	
	}

	@Override
	public void resize(int width, int height) {
		// change the stage's viewport when the screen size is changed
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		// dispose of assets when not needed anymore
		stage.dispose();
	}

}
