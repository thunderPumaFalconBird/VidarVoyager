package com.vv.game.rescueMission.entities.collectable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.vv.game.VidarVoyager;

/**
 *  This is the collectable abstract class. It is used by items that the player can pick up. The bodies must be static.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public abstract class Collectable extends Actor {
    protected final float inventoryOffsetX = 96;
    protected final World world;
    protected Body body;
    protected TextureRegion textureRegion;
    protected boolean collected = false;
    protected Vector2 inventoryPosition;
    protected float indexOffset;

    /**
     * The collectable constructor sets the box2d world.
     * @param world
     */
    public Collectable(World world){
        this.world = world;
    }

    /**
     * This method is used to create a static body in the box2d world.
     */
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

    /**
     * This method is used to deactivate the box2d body. When the box2d body is not active a collision event will not
     * happen. it is called when a player collects an item.
     */
    public void pickUpItem() { body.setActive(false); }

    /**
     * This method is used to reactive the box2d body when the player drops the item. The vector2 is the player position
     * which is used to set the x y location of the item to be slightly below the player.
     * @param vec2
     */
    public void putDownItem(Vector2 vec2) {
        collected = false;
        body.setTransform(vec2.x, (float) (vec2.y - 0.5), 0);
        body.setActive(true);
        setVisible(true);
    }

    public void setCollected(boolean collect) { collected = collect; }

    public boolean isCollected() { return collected; }

    public TextureRegion getTextureRegion(){ return textureRegion; }

    /**
     * This method is used to keep track of when the item was picked up.
     * @param vec2
     * @param index
     */
    public void setInventoryPosition(Vector2 vec2, int index){
        inventoryPosition = vec2;
        indexOffset = index*inventoryOffsetX;
    }
}
