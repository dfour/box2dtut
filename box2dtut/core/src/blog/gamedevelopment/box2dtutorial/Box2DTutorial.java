package blog.gamedevelopment.box2dtutorial;

import blog.gamedevelopment.box2dtutorial.loader.B2dAssetManager;
import blog.gamedevelopment.box2dtutorial.views.EndScreen;
import blog.gamedevelopment.box2dtutorial.views.LoadingScreen;
import blog.gamedevelopment.box2dtutorial.views.MainScreen;
import blog.gamedevelopment.box2dtutorial.views.MenuScreen;
import blog.gamedevelopment.box2dtutorial.views.PreferencesScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;

public class Box2DTutorial extends Game {
	
	//TODO PART DONE(-clamp) add walls on side to stop player going too far (clamp)
	//TODO PART DONE add enemy and enemy generation
	//TODO add obstacles
	//DONE add boosts
	//DONE(not pretty yet) add water that moves up at speed to encourage player to go faster
	//TODO add remove body from world code
	//TODO remove items off screen
	//TODO make enemies die if shot and fall offscreen(off screen should remove from abouve task)
	
	
	
	private LoadingScreen loadingScreen;
	private PreferencesScreen preferencesScreen;
	private MenuScreen menuScreen;
	private MainScreen mainScreen;
	private EndScreen endScreen;
	private AppPreferences preferences;
	public B2dAssetManager assMan = new B2dAssetManager();
	private Music playingSong;
	
	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int APPLICATION = 2;
	public final static int ENDGAME = 3;
	
	public int lastScore = 0;
	
	@Override
	public void create () {
		loadingScreen = new LoadingScreen(this);
		preferences = new AppPreferences();
		setScreen(loadingScreen);
		
		// tells our asset manger that we want to load the images set in loadImages method
		assMan.queueAddMusic();
		// tells the asset manager to load the images and wait until finished loading.
		assMan.manager.finishLoading();
		// loads the 2 sounds we use
		playingSong = assMan.manager.get("music/Rolemusic_-_pl4y1ng.mp3");
		
		//playingSong.play();
		
	}
	
	public void changeScreen(int screen){
		switch(screen){
			case MENU:
				if(menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case PREFERENCES:
				if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
				this.setScreen(preferencesScreen);
				break;
			case APPLICATION:
				// always make new game screen so game can't start midway
				if(mainScreen == null){
					mainScreen = new MainScreen(this);
				}else{
					mainScreen.resetWorld();
				}
				
				this.setScreen(mainScreen);
				break;
			case ENDGAME:
				if(endScreen == null) endScreen = new EndScreen(this);
				this.setScreen(endScreen);
				break;
		}
	}
	
	public AppPreferences getPreferences(){
		return this.preferences;
	}
	
	@Override
	public void dispose(){
		playingSong.dispose();
		assMan.manager.dispose();
	}
	
}
