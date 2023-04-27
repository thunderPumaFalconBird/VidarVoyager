package com.vv.game.rescueMission.entities.immovable;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.vv.game.VidarVoyager;
import com.vv.game.rescueMission.entities.movable.Lasers;

/**
 * This is the connon class. Cannons have a range that is used to fire the cannon when a playable character collides
 * with the range.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class Cannon extends Immovable {
    private Body rangeBody;
    private Array<Lasers> lasers;

    public Cannon(World world, RectangleMapObject object){
        super(world);
        Rectangle rectangle = object.getRectangle();
        setX((rectangle.getX() + rectangle.getWidth()/2) / VidarVoyager.PPM);
        setY((rectangle.getY() + rectangle.getHeight()/2) / VidarVoyager.PPM);

        createBody(object);
        lasers = new Array<>();
    }

    /**
     * This method is called when a playable character collides the cannon range.
     */
    public void fireCannon(){
        //TODO create lasers

    }

    /**
     * This method is called after the cannon is initialized. The range body is used by the collision handler to fire
     * the cannon.
     * @param object
     */
    public void setRangeBody(RectangleMapObject object){
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
    }
}