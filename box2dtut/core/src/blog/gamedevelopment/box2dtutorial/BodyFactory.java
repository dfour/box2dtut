package blog.gamedevelopment.box2dtutorial;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyFactory {
	public static final int STEEL = 0;
	public static final int WOOD = 1;
	public static final int RUBBER = 2;
	public static final int STONE = 3;
										
	/** //  **acebsp
	public static final short CATEGORY_PLAYER 			= 0b00000001;
	public static final short CATEGORY_SCENERY 			= 0b00000010;
	public static final short CATEGORY_BULLET			= 0b00000100; 
	public static final short CATEGORY_ENEMY 			= 0b00001000;
	public static final short CATEGORY_SCENERY_PASSIVE 	= 0b00010000;
	public static final short CATEGORY_AI_PLAYER 		= 0b00100000;
	
														//  **acebsp
	public static final short MASK_PLAYER 				= 0b00111110;
	public static final short MASK_SCENERY 				= 0b11111111;
	public static final short MASK_BULLET				= 0b00101011;
	public static final short MASK_ENEMY 				= 0b00110111;
	public static final short MASK_SCENERY_PASSIVE 		= 0b00111011;
	public static final short MASK_AI_PLAYER 			= 0b11111111;  // had this set to 00011110 and player, ai player did not contact with enemy 
	public static final short MASK_LIGHTS 				= 0b00000010;
	
	
	public static short currentMask = MASK_PLAYER;
	public static short currentCategory = CATEGORY_PLAYER;
	
	**/
	private static BodyFactory thisInstance;
	private World world;
	private final float DEGTORAD = 0.0174533f; 
		
	private BodyFactory(World world){
		this.world = world;
	}
	
	public static BodyFactory getInstance(World world){
		if(thisInstance == null){
			thisInstance = new BodyFactory(world);
		}else{
			thisInstance.world = world;
		}
		return thisInstance;
	}

	public Body makeBoxPolyBody(float posx, float posy, float width, float height,int material, BodyType bodyType){
		return makeBoxPolyBody(posx, posy, width, height, material, bodyType, false);
	}
	
	public Body makeBoxPolyBody(float posx, float posy, float width, float height,int material, BodyType bodyType, boolean fixedRotation){
		// create a definition
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyType;
		boxBodyDef.position.x = posx;
		boxBodyDef.position.y = posy;
		boxBodyDef.fixedRotation = fixedRotation;
		
		//create the body to attach said definition
		Body boxBody = world.createBody(boxBodyDef);
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width/2, height/2);
		boxBody.createFixture(makeFixture(material,poly));
		poly.dispose();

		return boxBody;
	}
	
	public Body makeCirclePolyBody(float posx, float posy, float radius, int material){
		return makeCirclePolyBody( posx,  posy,  radius,  material,  BodyType.DynamicBody,  false);
	}
	
	public Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyType bodyType){
		return makeCirclePolyBody( posx,  posy,  radius,  material,  bodyType,  false);
	}
	
	public Body makeBullet(float posx, float posy, float radius, int material, BodyType bodyType){
		Body body = makeCirclePolyBody( posx,  posy,  radius,  material,  bodyType,  false);
		for(Fixture fix :body.getFixtureList()){
			fix.setSensor(true);
		}
		body.setBullet(true);
		return body;
	}
	
	public Body makeCirclePolyBody(float posx, float posy, float radius, int material, BodyType bodyType, boolean fixedRotation){
		// create a definition
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyType;
		boxBodyDef.position.x = posx;
		boxBodyDef.position.y = posy;
		boxBodyDef.fixedRotation = fixedRotation;
		
		//create the body to attach said definition
		Body boxBody = world.createBody(boxBodyDef);
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(radius /2);
		boxBody.createFixture(makeFixture(material,circleShape));
		circleShape.dispose();
		return boxBody;
	}
	
	public Body makeSensorBody(float posx, float posy, float radius , BodyType bodyType){
		return makeSensorBody(posx,posy,radius,bodyType,false);
	}
	
	public Body makeSensorBody(float posx, float posy, float radius , BodyType bodyType, boolean fixedRotation){
		// create a definition
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyType;
		boxBodyDef.position.x = posx;
		boxBodyDef.position.y = posy;
		boxBodyDef.fixedRotation = fixedRotation;
		
		//create the body to attach said definition
		Body boxBody = world.createBody(boxBodyDef);
		this.makeSensorFixture(boxBody, radius);
		return boxBody;
	}
	
	static public FixtureDef makeFixture(int material, Shape shape){
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		
		switch(material){
		case STEEL:
			fixtureDef.density = 1f;
			fixtureDef.friction = 0.3f;
			fixtureDef.restitution = 0.1f;
			break;
		case WOOD:
			fixtureDef.density = 0.5f;
			fixtureDef.friction = 0.7f;
			fixtureDef.restitution = 0.3f;
			break;
		case RUBBER:
			fixtureDef.density = 1f;
			fixtureDef.friction = 0f;
			fixtureDef.restitution = 1f;
			break;
		case STONE:
			fixtureDef.density = 1f;
			fixtureDef.friction = 0.5f;
			fixtureDef.restitution = 0f;
			break;
		default:
				fixtureDef.density = 7f;
				fixtureDef.friction = 0.5f;
				fixtureDef.restitution = 0.3f;
		
		}

		return fixtureDef;
	}
	
	public void makeSensorFixture(Body body, float size){
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(size);
		fixtureDef.shape = circleShape;
		body.createFixture(fixtureDef);
		circleShape.dispose();
		
	}
	
	public void makeConeSensor(Body body, float size){
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		
		
		PolygonShape polygon = new PolygonShape();
		
		float radius = size;
		Vector2[] vertices = new Vector2[5];
		vertices[0] = new Vector2(0,0);
		for (int i = 2; i < 6; i++) {
		    float angle = (float) (i  / 6.0 * 145 * DEGTORAD); // convert degrees to radians
		    vertices[i-1] = new Vector2( radius * ((float)Math.cos(angle)), radius * ((float)Math.sin(angle)));
		}
		polygon.set(vertices);
		//polygon.setRadius(size);
		fixtureDef.shape = polygon;
		body.createFixture(fixtureDef);
		polygon.dispose();
	}
	
	/*
	 * Make a body from a set of vertices
	 */
	public Body makePolygonShapeBody(Vector2[] vertices, float posx, float posy, int material, BodyType bodyType){
		BodyDef boxBodyDef = new BodyDef();
		boxBodyDef.type = bodyType;
		boxBodyDef.position.x = posx;
		boxBodyDef.position.y = posy;
		Body boxBody = world.createBody(boxBodyDef);
		
		PolygonShape polygon = new PolygonShape();
		polygon.set(vertices);
		boxBody.createFixture(makeFixture(material,polygon));
		polygon.dispose();
		
		return boxBody;
	}
	
	public void makeAllFixturesSensors(Body bod){
		for(Fixture fix :bod.getFixtureList()){
			fix.setSensor(true);
		}
	}
	
	public void setAllFixtureMask(Body bod, Short filter){
		Filter fil = new Filter();
		fil.groupIndex = filter;
		for(Fixture fix :bod.getFixtureList()){
			fix.setFilterData(fil);
		}
	}
	
	public Body addCircleFixture(Body bod, float x, float y, float size, int material, boolean sensor){
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(size /2);
		circleShape.setPosition(new Vector2(x,y));
		FixtureDef fix = makeFixture(material,circleShape);
		fix.isSensor = sensor;
		bod.createFixture(fix);
		circleShape.dispose();
		return bod;
	}
	
}