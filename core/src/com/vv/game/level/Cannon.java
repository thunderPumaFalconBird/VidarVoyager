package com.vv.game.level;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vv.game.VidarVoyager;

public class Cannon extends Actor {
    //this enum will be used in collisionHandler to identify a rangeBody by its user data.
    public static enum Type{
        RANGE
    }
    private final World world;
    private Body body;
    private Body rangeBody;

    public Cannon(World world, RectangleMapObject object){
        this.world = world;
        Rectangle rectangle = object.getRectangle();
        setX((rectangle.getX() + rectangle.getWidth()/2) / VidarVoyager.PPM);
        setY((rectangle.getY() + rectangle.getHeight()/2) / VidarVoyager.PPM);

        createBody(rectangle);
    }

    public void fireCannon(){
        //TODO create lasers

    }

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
        rangeBody.setUserData(Type.RANGE);
    }

    public void destroyRangeBody(){ world.destroyBody(rangeBody);}

    public void createBody(Rectangle rectangle){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(getX(), getY());

        body = world.createBody(bdef);

        shape.setAsBox(rectangle.getWidth() / 2 / VidarVoyager.PPM,
                rectangle.getHeight() / 2 / VidarVoyager.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
    }

    public Body getBody(){ return body; }

    public void destroyBody() { world.destroyBody(body); }
}