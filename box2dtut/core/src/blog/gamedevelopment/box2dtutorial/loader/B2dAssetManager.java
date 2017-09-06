package blog.gamedevelopment.box2dtutorial.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader.ParticleEffectParameter;

public class B2dAssetManager {
	
	public final AssetManager manager = new AssetManager();

	// Sounds
	public final String boingSound = "sounds/boing.wav";
	public final String pingSound = "sounds/ping.wav";
	
	// Music
	public final String playingSong = "music/Rolemusic_-_pl4y1ng.mp3";
	
	// Skin
	public final String skin = "skin/glassy-ui.json";
	
	// Textures
	public final String gameImages = "images/game.atlas";
	public final String loadingImages = "images/loading.atlas";
	
	// Particle Effects
	public final String smokeEffect = "particles/smoke.pe";
	public final String waterEffect = "particles/water.pe";
	public final String fireEffect = "particles/fire.pe";
	
	public void queueAddFonts(){
		
	}
	
	public void queueAddParticleEffects(){
		ParticleEffectParameter pep = new ParticleEffectParameter();
		pep.atlasFile = "images/game.atlas";
		manager.load(smokeEffect, ParticleEffect.class, pep);
		manager.load(waterEffect, ParticleEffect.class, pep);
		manager.load(fireEffect, ParticleEffect.class, pep);
	}
	
	public void queueAddImages(){
		manager.load(gameImages, TextureAtlas.class);
	}
	
	// a small set of images used by the loading screen
	public void queueAddLoadingImages(){
		manager.load(loadingImages, TextureAtlas.class);
	}
	
	public void queueAddSkin(){
		SkinParameter params = new SkinParameter("skin/glassy-ui.atlas");
		manager.load(skin, Skin.class, params);
		
	}
	
	public void queueAddMusic(){
		manager.load(playingSong, Music.class);
	}
	
	public void queueAddSounds(){
		manager.load(boingSound, Sound.class);
		manager.load(pingSound, Sound.class);
	}
}
