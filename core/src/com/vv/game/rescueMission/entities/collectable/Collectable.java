package com.vv.game.rescueMission.entities.collectable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vv.game.VidarVoyager;

public abstract class Collectable extends Actor {
    protected final float inventoryOffsetX = 96;
    protected final float inventoryOffsetY = 60;
    protected final World world;
    protected Body body;
    protected boolean collected = false;
    protected Vector2 inventoryPosition;
    protected float indexOffset;

    public Collectable(World world){
        this.world = world;
    }


    public void createBody(){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(getX(), getY());

        body = world.createBody(bdef);

        shape.setAsBox(getWidth() / 2 / VidarVoyager.PPM,
                getHeight() / 2 / VidarVoyager.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
        shape.dispose();
    }

    public Body getBody(){ return body; }

    public void pickUpItem() {
        body.setActive(false);
    }

    public void putDownItem(Vector2 vec2) {
        collected = false;
        body.setTransform(vec2.x, (float) (vec2.y - 0.5), 0);
        body.setActive(true);
        setVisible(true);
    }

    public void setCollected(boolean collect) { collected = collect; }

    public boolean isCollected() { return collected; }

    public void setInventoryPosition(Vector2 vec2, int index){
        inventoryPosition = vec2;
        indexOffset = index*inventoryOffsetX;
        //TODO figure out the pixel location based on the array index. Each inventory space should be 100 pixels...should
    }

    public void redefineBody(float x, float y) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x, y);

        body = world.createBody(bdef);

        shape.setAsBox(getWidth() / 2 / VidarVoyager.PPM,
                getHeight() / 2 / VidarVoyager.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
        shape.dispose();
    }
}
