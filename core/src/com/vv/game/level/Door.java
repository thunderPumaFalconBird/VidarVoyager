package com.vv.game.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vv.game.VidarVoyager;

import java.io.File;

public class Door extends Actor {
    private final World world;
    private Body body;
    private final Texture doorTexture;
    private boolean active = false;
    private boolean unlocked = false;

    public Door(World world, RectangleMapObject object){
        this.world = world;
        Rectangle rectangle = object.getRectangle();
        setX((rectangle.getX() + rectangle.getWidth()/2) / VidarVoyager.PPM);
        setY((rectangle.getY() + rectangle.getHeight()/2) / VidarVoyager.PPM);

        createBody(rectangle);

        doorTexture = new Texture("maps" + File.separator + "doorClosed.png");
    }

    public void setActive(boolean active){ this.active = active; }

    public boolean isActive(){ return active; }

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
        body.setUserData(this);
    }

    public Body getBody(){ return body; }

    public void destroyBody() { world.destroyBody(body); }

    @Override
    public void draw(Batch batch, float parentAlpha){
        if(!unlocked) {
            batch.draw(doorTexture, body.getPosition().x * VidarVoyager.PPM - ((float) doorTexture.getWidth() / 2),
                    body.getPosition().y * VidarVoyager.PPM - ((float) doorTexture.getHeight() / 2));
            if(active){
                //TODO implement puzzles
                //draw puzzle
                //check puzzle for win
                world.destroyBody(body);
                active = false;
                unlocked = true;
            }
        }
    }

    public void dispose(){ doorTexture.dispose(); }
}