package com.vv.game.rescueMission.entities.immovable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.vv.game.VidarVoyager;

import java.io.File;

/**
 * This is the connon class. Cannons have a range that is used to fire the cannon when a playable character collides
 * with the range.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class Cannon extends Immovable {
    public enum Orientation { UP, DOWN, LEFT, RIGHT }
    private Body rangeBody;
    private Array<TextureRegion> laserAnimation;
    private boolean active = false;
    private int currentFrame = 0;
    private int halfWidth, halfHeight;
    private Orientation orientation;

    public Cannon(World world, RectangleMapObject object){
        super(world);
        Rectangle rectangle = object.getRectangle();
        setX((rectangle.getX() + rectangle.getWidth()/2) / VidarVoyager.PPM);
        setY((rectangle.getY() + rectangle.getHeight()/2) / VidarVoyager.PPM);

        createBody(object);

        TextureAtlas textureAtlas = new TextureAtlas("laser" + File.separator + "Laser.txt");
        laserAnimation = new Array<TextureRegion>(textureAtlas.getRegions());

        halfWidth = laserAnimation.get(0).getRegionWidth() / 2;
        halfHeight = laserAnimation.get(0).getRegionHeight() / 2;
    }

    /**
     * This method is called when a playable character collides the cannon range.
     */
    public void fireCannon(){
        //TODO create lasers
        active = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(active){
            batch.draw(laserAnimation.get(currentFrame++), rangeBody.getPosition().x*VidarVoyager.PPM - halfWidth,
                    rangeBody.getPosition().y*VidarVoyager.PPM - halfHeight);
        }
        if(currentFrame > 5){
            currentFrame = 0;
        }
    }

    /**
     * This method is called after the cannon is initialized. The range body is used by the collision handler to fire
     * the cannon.
     * @param object
     */
    public void setRangeBody(RectangleMapObject object, Orientation orientation){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        Rectangle rectangle = object.getRectangle();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((rectangle.getX() + rectangle.getWidth()/2) / VidarVoyager.PPM,
                (rectangle.getY() + rectangle.getHeight()/2) / VidarVoyager.PPM);

        rangeBody = world.createBody(bdef);

        shape.setAsBox(rectangle.getWidth() / 2 / VidarVoyager.PPM,
                rectangle.getHeight() / 2 / VidarVoyager.PPM);
        fdef.shape = shape;
        fdef.isSensor = true; //This allows the player to pass through the object.
        rangeBody.createFixture(fdef);
        rangeBody.setUserData(this);

        //TODO set animation and x, y position based on orientation. Create rotated animation for left and right.
        this.orientation = orientation;
    }
}