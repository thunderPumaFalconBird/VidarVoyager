package com.vv.game.rescueMission.entities.immovable;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vv.game.VidarVoyager;

/**
 * This is the immovable abstract class. It is used by objects that do not move.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public abstract class Immovable extends Actor {
    protected final World world;
    protected Body body;

    /**
     * The immovable constructor sets the box2d world.
     * @param world
     */
    public Immovable(World world){
        this.world = world;
    }

    /**
     * This method is called to create a static body in the box2d world.
     * @param object
     */
    public void createBody(RectangleMapObject object){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        Rectangle rectangle = object.getRectangle();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(getX(), getY());

        body = world.createBody(bdef);

        shape.setAsBox(rectangle.getWidth() / 2 / VidarVoyager.PPM,
                rectangle.getHeight() / 2 / VidarVoyager.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
        shape.dispose();
    }

    public Body getBody(){ return body; }
}
