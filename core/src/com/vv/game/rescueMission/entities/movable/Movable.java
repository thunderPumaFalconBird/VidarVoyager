package com.vv.game.rescueMission.entities.movable;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vv.game.VidarVoyager;

/**
 * This is the Movable abstract class. Any entity that moves in the world will need to have a dynamic body.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public abstract class Movable extends Actor {
    protected final World world;
    protected Body body;

    public abstract void update();

    public Movable(World world){
        this.world = world;
    }

    /**
     * This method creates a dynamic body that is used by the box2d world to apply physics.
     */
    public void createBody(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth()/2)/VidarVoyager.PPM, (getHeight()/2)/VidarVoyager.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
        shape.dispose();
    }

    public Body getBody(){ return body; }
}
