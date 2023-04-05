package com.vv.game.rescueMission.entities.immovable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.vv.game.VidarVoyager;

import java.io.File;

public class Door extends Immovable {
    private final Texture doorTexture;
    private boolean active = false;
    private boolean unlocked = false;

    public Door(World world, RectangleMapObject object){
        super(world);
        Rectangle rectangle = object.getRectangle();
        setX((rectangle.getX() + rectangle.getWidth()/2) / VidarVoyager.PPM);
        setY((rectangle.getY() - 25 + rectangle.getHeight()/2) / VidarVoyager.PPM);

        createBody(object);
        body.setUserData(this);

        doorTexture = new Texture("maps" + File.separator + "doorClosed300.png");
    }

    public void setActive(boolean active){ this.active = active; }

    public boolean isActive(){ return active; }

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